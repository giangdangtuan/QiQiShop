package com.app85soft.qiqishop.services.product;

import com.app85soft.qiqishop.component.Translator;
import com.app85soft.qiqishop.dto.constant.ActiveStatus;
import com.app85soft.qiqishop.dto.request.IdsRequest;
import com.app85soft.qiqishop.dto.request.product.AddProductReq;
import com.app85soft.qiqishop.dto.request.product.UpdateProductReq;
import com.app85soft.qiqishop.dto.response.BaseResponse;
import com.app85soft.qiqishop.dto.response.product.ProductRes;
import com.app85soft.qiqishop.dto.response.user.UserDetailRes;
import com.app85soft.qiqishop.entities.model.Model;
import com.app85soft.qiqishop.entities.product.Product;
import com.app85soft.qiqishop.entities.role.constant.PermissionKey;
import com.app85soft.qiqishop.entities.role.constant.PermissionType;
import com.app85soft.qiqishop.entities.user.User;
import com.app85soft.qiqishop.exceptions.BusinessException;
import com.app85soft.qiqishop.repositories.model.ModelRepository;
import com.app85soft.qiqishop.repositories.product.ProductRepository;
import com.app85soft.qiqishop.services.BaseService;
import com.app85soft.qiqishop.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl extends BaseService implements ProductService {
    private final ProductRepository productRepository;
    private final ModelRepository modelRepository;

    @Override
    public ProductRes addProduct(AddProductReq productReq) {
        User user = getUser(PermissionKey.CREATE, PermissionType.PRODUCT);
        Product product = new Product();
        product.setCode(generateCode(8,1));
        product.setName(productReq.getName());
        product.setCategoryId(productReq.getCategoryId());
        product.setCoverImage(productReq.getCoverImage());
        product.setDescription(productReq.getDescription());
        product.setStatus(ActiveStatus.ACTIVE);
        productRepository.save(product);

        List<Model> models = new ArrayList<>();

        if(productReq.getModels() != null) {
            for(AddProductReq.Model item : productReq.getModels()) {
                Model model = new Model();
                model.setCode(generateCode(8,2));
                model.setName(item.getName());
                model.setProductId(product.getId());
                model.setCoverImage(productReq.getCoverImage());
                model.setPrice(item.getPrice());
                model.setStock(item.getStock());
                models.add(model);
            }
        } else {
            Model model = new Model();
            model.setCode(generateCode(8,2));
            model.setPrice(productReq.getPrice());
            model.setProductId(product.getId());
            model.setStock(productReq.getStock());
            models.add(model);
        }
        modelRepository.saveAll(models);

        ProductRes productRes = getProductRes(product, models);

        return productRes;
    }

    @Override
    public ProductRes updateProduct(UpdateProductReq productReq) {
        User user = getUser(PermissionKey.CREATE, PermissionType.PRODUCT);
        Product product = productRepository.getProductToUpdate(productReq.getId());

        if (product == null) {
            throw new BusinessException(Translator.toLocale("product_id_not_exist"));
        }
        if (productReq.getName() != null && !productReq.getName().isEmpty()) {
            product.setName(productReq.getName());
        }
        if (productReq.getCategoryId() != null) {
            product.setCategoryId(productReq.getCategoryId());
        }
        if (productReq.getCoverImage() != null) {
            product.setCoverImage(productReq.getCoverImage());
        }
        if (productReq.getDescription() != null && !productReq.getDescription().isEmpty()) {
            product.setDescription(productReq.getDescription());
        }
        if (productReq.getStatus() != null) {
            product.setStatus(productReq.getStatus());
        }
        productRepository.save(product);

        List<Model> updatedModels = new ArrayList<>();
        List<Model> existingModels = modelRepository.findAllByProductId(product.getId());

        Map<String, Model> existingModelMap = existingModels.stream()
                .collect(Collectors.toMap(Model::getCode, m -> m));

        if (productReq.getModels() != null && !productReq.getModels().isEmpty()) {
            for(UpdateProductReq.Model item : productReq.getModels()) {
                Model model;
                if (item.getCode() != null && existingModelMap.containsKey(item.getCode())) {
                    model = existingModelMap.get(item.getCode());
                    existingModelMap.remove(item.getCode());
                } else {
                    model = new Model();
                    model.setCode(generateCode(8, 2));
                    model.setProductId(product.getId());
                }
                model.setName(item.getName());
                model.setCoverImage(item.getCoverImage());
                model.setPrice(item.getPrice());
                model.setStock(item.getStock());
                model.setDeleted(false);

                updatedModels.add(model);
            }

            if (!existingModelMap.isEmpty()) {
                modelRepository.softDeleteModels(existingModelMap.values());
            }
        } else {
            if (!existingModels.isEmpty()) {
                Model existingModel = existingModels.stream()
                        .filter(model -> !model.isDeleted() && model.getName() != null && !model.getName().isEmpty())
                        .findFirst()
                        .orElse(null);

                if (existingModel != null) {
                    modelRepository.softDeleteModels(existingModels);

                    Model newModel = new Model();
                    newModel.setProductId(product.getId());
                    newModel.setCode(generateCode(8, 2));
                    newModel.setPrice(productReq.getPrice());
                    newModel.setStock(productReq.getStock());
                    updatedModels.add(newModel);
                } else {
                    existingModel.setPrice(productReq.getPrice());
                    existingModel.setStock(productReq.getStock());
                    updatedModels.add(existingModel);
                }
            } else {
                Model newModel = new Model();
                newModel.setProductId(product.getId());
                newModel.setCode(generateCode(8, 2));
                newModel.setPrice(productReq.getPrice());
                newModel.setStock(productReq.getStock());
                updatedModels.add(newModel);
            }
        }
        modelRepository.saveAll(updatedModels);
        ProductRes productRes = getProductRes(product, updatedModels);

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
        User user = getUser(PermissionKey.READ, PermissionType.PRODUCT);
        long countProduct = productRepository.countProduct(status, name, categoryId);
        List<ProductRes> listProducts = productRepository.getProduct(status, name, categoryId, page);
        return new BaseResponse<>(listProducts, countProduct, page);
    }

    @Override
    public BaseResponse<ProductRes> getProduct(int productId) {
        User user = getUser(PermissionKey.READ, PermissionType.PRODUCT);

        ProductRes productRes = productRepository.getProductDetail(productId);
        if (productRes == null) {
            throw new BusinessException(Translator.toLocale("id_not_exist"), HttpStatus.NOT_FOUND);
        }
        return new BaseResponse<>(productRes);
    }

    private ProductRes getProductRes(Product product, List<Model> models) {

        return ProductRes.builder()
                .id(product.getId())
                .code(product.getCode())
                .name(product.getName())
                .categoryId(product.getCategoryId())
                .coverImage(product.getCoverImage())
                .description(product.getDescription())
                .status(product.getStatus())
                .models(modelRepository.getModels(product.getId()))
                .build();
    }

    private String generateCode(int count, int obj) {
        String code;
        boolean exists;

        do {
            if(obj == 1) {
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
