package com.app85soft.qiqishop.services.product;

import com.app85soft.qiqishop.component.Translator;
import com.app85soft.qiqishop.dto.constant.ActiveStatus;
import com.app85soft.qiqishop.dto.request.IdsRequest;
import com.app85soft.qiqishop.dto.request.model.AddModelReq;
import com.app85soft.qiqishop.dto.request.model.VariantOptionReq;
import com.app85soft.qiqishop.dto.request.model.VariantValueReq;
import com.app85soft.qiqishop.dto.request.product.AddProductReq;
import com.app85soft.qiqishop.dto.request.product.ProductImageReq;
import com.app85soft.qiqishop.dto.request.product.UpdateProductReq;
import com.app85soft.qiqishop.dto.response.BaseResponse;
import com.app85soft.qiqishop.dto.response.product.ProductDetailRes;
import com.app85soft.qiqishop.dto.response.product.ProductRes;
import com.app85soft.qiqishop.entities.model.Model;
import com.app85soft.qiqishop.entities.model.VariantOption;
import com.app85soft.qiqishop.entities.model.VariantValue;
import com.app85soft.qiqishop.entities.product.Product;
import com.app85soft.qiqishop.entities.product.ProductImage;
import com.app85soft.qiqishop.entities.role.constant.PermissionKey;
import com.app85soft.qiqishop.entities.role.constant.PermissionType;
import com.app85soft.qiqishop.entities.user.User;
import com.app85soft.qiqishop.exceptions.BusinessException;
import com.app85soft.qiqishop.repositories.model.ModelRepository;
import com.app85soft.qiqishop.repositories.product.ProductImageRepository;
import com.app85soft.qiqishop.repositories.product.ProductRepository;
import com.app85soft.qiqishop.repositories.variant.VariantOptionRepository;
import com.app85soft.qiqishop.repositories.variant.VariantValueRepository;
import com.app85soft.qiqishop.services.BaseService;
import com.app85soft.qiqishop.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl extends BaseService implements ProductService {
    private final ProductRepository productRepository;
    private final ModelRepository modelRepository;
    private final ProductImageRepository productImageRepository;
    private final VariantOptionRepository variantOptionRepository;
    private final VariantValueRepository variantValueRepository;

    @Override
    public ProductRes addProduct(AddProductReq req) {
        User user = getUser(PermissionKey.CREATE, PermissionType.PRODUCT);
        if (productRepository.existsByName(req.getName())) {
            throw new BusinessException("name_already_exists", HttpStatus.BAD_REQUEST);
        }

        Product product = new Product();
        product.setCode(generateCode(8, 1));
        product.setName(req.getName());
        product.setCategoryId(req.getCategoryId());
        product.setCoverImage(req.getCoverImage());
        product.setDescription(req.getDescription());
        product.setWeight(req.getWeight());
        product.setStatus(ActiveStatus.ACTIVE);
        productRepository.save(product);

        Map<String, Integer> variantValueMap = new HashMap<>();

        // Nếu có phân loại
        if (req.getVariantOptions() != null && !req.getVariantOptions().isEmpty()) {
            for (VariantOptionReq optionReq : req.getVariantOptions()) {
                VariantOption option = new VariantOption();
                option.setName(optionReq.getName());
                option.setProductId(product.getId());
                option.setDeleted(false);
                variantOptionRepository.save(option);

                for (VariantValueReq valueReq : optionReq.getValues()) {
                    VariantValue value = new VariantValue();
                    value.setValue(valueReq.getValue());
                    value.setOptionTypeId(option.getId());
                    value.setDeleted(false);
                    variantValueRepository.save(value);
                    variantValueMap.put(value.getValue(), value.getId());
                }
            }

            // Xử lý các model phân loại
            List<Model> models = new ArrayList<>();
            for (AddModelReq modelReq : req.getModels()) {
                String[] parts = modelReq.getName().split("-");

                Integer optionValue1Id = null;
                Integer optionValue2Id = null;

                if (req.getVariantOptions().size() == 1) {
                    if (parts.length != 1) {
                        throw new BusinessException("Tên phân loại không đúng định dạng (ví dụ: 'Đỏ')", HttpStatus.BAD_REQUEST);
                    }
                    optionValue1Id = variantValueMap.get(parts[0]);
                    if (optionValue1Id == null) {
                        throw new BusinessException("Không tìm thấy giá trị phân loại: " + parts[0], HttpStatus.BAD_REQUEST);
                    }
                } else if (req.getVariantOptions().size() == 2) {
                    if (parts.length != 2) {
                        throw new BusinessException("Tên phân loại không đúng định dạng (ví dụ: 'Đỏ-S')", HttpStatus.BAD_REQUEST);
                    }
                    optionValue1Id = variantValueMap.get(parts[0]);
                    optionValue2Id = variantValueMap.get(parts[1]);
                    if (optionValue1Id == null || optionValue2Id == null) {
                        throw new BusinessException("Không tìm thấy giá trị phân loại: " + modelReq.getName(), HttpStatus.BAD_REQUEST);
                    }
                } else {
                    throw new BusinessException("Hệ thống hiện chỉ hỗ trợ tối đa 2 phân loại", HttpStatus.BAD_REQUEST);
                }

                Model model = new Model();
                model.setCode(generateCode(8, 2));
                model.setName(modelReq.getName());
                model.setProductId(product.getId());
                model.setPrice(modelReq.getPrice());
                model.setStock(modelReq.getStock());
                model.setOptionValue1Id(optionValue1Id);
                model.setOptionValue2Id(optionValue2Id);
                models.add(model);
            }
            modelRepository.saveAll(models);
        } else {
            Model model = new Model();
            model.setCode(generateCode(8, 2));
            model.setProductId(product.getId());
            model.setPrice(req.getPrice());
            model.setStock(req.getStock());
            modelRepository.save(model);
        }

        // Lưu ảnh sản phẩm
        if (req.getImages() != null) {
            List<ProductImage> productImages = req.getImages().stream().map(img -> {
                ProductImage pi = new ProductImage();
                pi.setProductId(product.getId());
                pi.setImageId(img.getImageId());
                pi.setSortOrder(img.getSortOrder());
                return pi;
            }).toList();
            productImageRepository.saveAll(productImages);
        }

        return getProductRes(product);
    }


    @Override
    public ProductRes updateProduct(UpdateProductReq productReq) {
        User user = getUser(PermissionKey.CREATE, PermissionType.PRODUCT);
        Product product = productRepository.getProductToUpdate(productReq.getId());

        if (product == null) {
            throw new BusinessException(Translator.toLocale("product_id_not_exist"));
        }
        if (productRepository.existsByName(productReq.getName(), product.getId())) {
            throw new BusinessException(Translator.toLocale("name_already_exists"), HttpStatus.BAD_REQUEST);
        }

        if (productReq.getName() != null && !productReq.getName().isEmpty()) {
            product.setName(productReq.getName());
        }
        if (productReq.getCategoryId() != null) {
            product.setCategoryId(productReq.getCategoryId());
        }
        if (productReq.getWeight() != null) {
            product.setWeight(productReq.getWeight());
        }
        if (productReq.getDescription() != null && !productReq.getDescription().isEmpty()) {
            product.setDescription(productReq.getDescription());
        }
        if (productReq.getStatus() != null) {
            product.setStatus(productReq.getStatus());
        }
        if (productReq.getCoverImage() != null && productReq.getCoverImage() != 0) {
            product.setCoverImage(productReq.getCoverImage());
        }
        productRepository.save(product);

        if (productReq.getVariantOptions() != null && !productReq.getVariantOptions().isEmpty()) {
            // Xử lý soft delete VariantOption
            List<VariantOption> oldOptions = variantOptionRepository.findByProductId(product.getId());
            Set<Integer> reqOptionIds = productReq.getVariantOptions().stream()
                    .map(VariantOptionReq::getId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            for (VariantOption old : oldOptions) {
                if (!reqOptionIds.contains(old.getId())) {
                    old.setDeleted(true);
                    variantOptionRepository.save(old);
                }
            }

            // Tạo lại các VariantOption mới và VariantValue
            for (VariantOptionReq optionReq : productReq.getVariantOptions()) {
                VariantOption option;
                if (optionReq.getId() != null) {
                    option = variantOptionRepository.findById(optionReq.getId())
                            .orElseThrow(() -> new BusinessException("Không tìm thấy option id: " + optionReq.getId()));
                } else {
                    option = new VariantOption();
                    option.setProductId(product.getId());
                    option.setDeleted(false);
                }

                option.setName(optionReq.getName());
                option.setDeleted(false);
                variantOptionRepository.save(option);

                List<VariantValue> oldValues = variantValueRepository.findByOptionTypeId(option.getId());
                Set<Integer> reqValueIds = optionReq.getValues().stream()
                        .map(VariantValueReq::getId)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet());

                for (VariantValue old : oldValues) {
                    if (!reqValueIds.contains(old.getId())) {
                        old.setDeleted(true);
                        variantValueRepository.save(old);
                    }
                }

                for (VariantValueReq valueReq : optionReq.getValues()) {
                    VariantValue value;
                    if (valueReq.getId() != null) {
                        value = variantValueRepository.findById(valueReq.getId())
                                .orElseThrow(() -> new BusinessException("Không tìm thấy value id: " + valueReq.getId()));
                    } else {
                        value = new VariantValue();
                        value.setOptionTypeId(option.getId());
                        value.setDeleted(false);
                    }

                    value.setValue(valueReq.getValue());
                    value.setDeleted(false);
                    variantValueRepository.save(value);
                }
            }

            // Ánh xạ VariantValue theo value text
            Map<String, VariantValue> valueMap = variantValueRepository.findByProductId(product.getId()).stream()
                    .collect(Collectors.toMap(v -> v.getValue().trim().toLowerCase(), v -> v));

            List<VariantOption> variantOptions = variantOptionRepository.findByProductId(product.getId()).stream()
                    .filter(option -> !option.isDeleted())
                    .toList();

            VariantOption option1 = !variantOptions.isEmpty() ? variantOptions.get(0) : null;
            VariantOption option2 = variantOptions.size() > 1 ? variantOptions.get(1) : null;

            // Cập nhật Model
            Map<Integer, Model> oldModelMap = modelRepository.findAllByProductId(product.getId()).stream()
                    .filter(model -> !model.isDeleted())
                    .collect(Collectors.toMap(Model::getId, m -> m));

            List<Model> updatedModels = new ArrayList<>();
            Set<Integer> reqModelIds = new HashSet<>();

            for (AddModelReq modelReq : productReq.getModels()) {
                Model model;
                if (modelReq.getId() != null) {
                    model = oldModelMap.get(modelReq.getId());
                    if (model == null) {
                        throw new BusinessException("Không tìm thấy model id: " + modelReq.getId());
                    }
                    reqModelIds.add(modelReq.getId());
                } else {
                    model = new Model();
                    model.setCode(generateCode(8, 2));
                    model.setProductId(product.getId());
                }

                model.setName(modelReq.getName());
                model.setPrice(modelReq.getPrice());
                model.setStock(modelReq.getStock());
                model.setDeleted(false);

                // Tách tên: "Đỏ-S" => ["Đỏ", "S"]
                String[] parts = modelReq.getName().split("-");
                String part1 = parts.length > 0 ? parts[0].trim().toLowerCase() : null;
                String part2 = parts.length > 1 ? parts[1].trim().toLowerCase() : null;

                Integer optionValue1Id = (part1 != null && valueMap.containsKey(part1)) ? valueMap.get(part1).getId() : null;
                Integer optionValue2Id = (part2 != null && valueMap.containsKey(part2)) ? valueMap.get(part2).getId() : null;

                model.setOptionValue1Id(optionValue1Id);
                model.setOptionValue2Id(optionValue2Id);

                updatedModels.add(model);
            }

            modelRepository.saveAll(updatedModels);

            // Soft delete các model không còn trong request
            for (Model oldModel : oldModelMap.values()) {
                if (!reqModelIds.contains(oldModel.getId())) {
                    oldModel.setDeleted(true);
                    modelRepository.save(oldModel);
                }
            }
        } else {
            List<VariantOption> variantOptions = variantOptionRepository.findByProductId(product.getId());
            for (VariantOption option : variantOptions) {
                option.setDeleted(true);
                variantOptionRepository.save(option);
            }

            List<VariantValue> variantValues = variantValueRepository.findByProductId(product.getId());
            for (VariantValue value : variantValues) {
                value.setDeleted(true);
                variantValueRepository.save(value);
            }

            List<Model> oldModels = modelRepository.findAllByProductId(product.getId());

            Model model = oldModels.isEmpty() ? new Model() : oldModels.getFirst();
            model.setCode(model.getCode() == null ? generateCode(8, 2) : model.getCode());
            model.setProductId(product.getId());
            model.setPrice(productReq.getPrice());
            model.setStock(productReq.getStock());
            model.setDeleted(false);
            modelRepository.save(model);

            for (int i = 1; i < oldModels.size(); i++) {
                oldModels.get(i).setDeleted(true);
                modelRepository.save(oldModels.get(i));
            }
        }

        if (productReq.getImages() != null) {
            Set<Integer> newImageIds = productReq.getImages().stream()
                    .map(ProductImageReq::getImageId)
                    .collect(Collectors.toSet());

            List<ProductImage> existingImages = productImageRepository.findByProductId(product.getId());

            Map<Integer, ProductImageReq> imageReqMap = productReq.getImages().stream()
                    .collect(Collectors.toMap(ProductImageReq::getImageId, Function.identity()));

            List<ProductImage> imagesToSave = new ArrayList<>();

            for (ProductImage img : existingImages) {
                if (newImageIds.contains(img.getImageId())) {
                    img.setDeleted(false);
                    img.setSortOrder(imageReqMap.get(img.getImageId()).getSortOrder());
                } else {
                    img.setDeleted(true);
                }
                imagesToSave.add(img);
            }

            Set<Integer> existingImageIds = existingImages.stream()
                    .map(ProductImage::getImageId)
                    .collect(Collectors.toSet());

            List<ProductImage> newImages = productReq.getImages().stream()
                    .filter(req -> !existingImageIds.contains(req.getImageId()))
                    .map(req -> {
                        ProductImage pi = new ProductImage();
                        pi.setProductId(product.getId());
                        pi.setImageId(req.getImageId());
                        pi.setSortOrder(req.getSortOrder());
                        pi.setDeleted(false);
                        return pi;
                    }).toList();

            imagesToSave.addAll(newImages);
            productImageRepository.saveAll(imagesToSave);
        }


        ProductRes productRes = getProductRes(product);

        return productRes;
    }

    @Override
    public List<Integer> deleteProducts(IdsRequest req) {
        User user = getUser(PermissionKey.DECISION, PermissionType.PRODUCT);
        List<Integer> productIds = req.getIds();
        List<Integer> existingIds = productRepository.getAllIdToCheckExist(productIds);
        List<Integer> nonExistingIds = productIds.stream().filter(id -> !existingIds.contains(id)).toList();
        if (!nonExistingIds.isEmpty()) {
            throw new BusinessException(Translator.toLocale("id_not_exist"), HttpStatus.BAD_REQUEST);
        }
        productRepository.deleteProducts(productIds);
        return productIds;
    }

    @Override
    public BaseResponse<List<ProductRes>> getProducts(ActiveStatus status, String name, Integer categoryId, int page) {
//        User user = getUser(PermissionKey.READ, PermissionType.PRODUCT);
        long countProduct = productRepository.countProduct(status, name, categoryId);
        List<ProductRes> listProducts = productRepository.getProduct(status, name, categoryId, page);
        return new BaseResponse<>(listProducts, countProduct, page);
    }

    @Override
    public BaseResponse<ProductDetailRes> getProduct(int productId) {
//        User user = getUser(PermissionKey.READ, PermissionType.PRODUCT);

        ProductDetailRes productRes = productRepository.getProductDetail(productId);
        if (productRes == null) {
            throw new BusinessException(Translator.toLocale("id_not_exist"), HttpStatus.NOT_FOUND);
        }
        return new BaseResponse<>(productRes);
    }

    private ProductRes getProductRes(Product product) {

        return ProductRes.builder()
                .id(product.getId())
                .code(product.getCode())
                .name(product.getName())
                .categoryId(product.getCategoryId())
                .coverImage(product.getCoverImage())
                .weight(product.getWeight())
                .description(product.getDescription())
                .status(product.getStatus())
                .models(modelRepository.getModels(product.getId()))
                .build();
    }

    private String generateCode(int count, int obj) {
        String code;
        boolean exists;

        do {
            if (obj == 1) {
                code = Util.randomString(count);
                exists = productRepository.existsByCode(code);
            } else {
                code = Util.randomString(count);
                exists = modelRepository.existsByCode(code);
            }

        } while (exists);
        return code;
    }
}
