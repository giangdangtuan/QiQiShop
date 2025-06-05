package com.app85soft.qiqishop.services.cart;

import com.app85soft.qiqishop.component.Translator;
import com.app85soft.qiqishop.dto.request.IdsRequest;
import com.app85soft.qiqishop.dto.request.cart.AddToCartReq;
import com.app85soft.qiqishop.dto.request.cart.UpdateCartReq;
import com.app85soft.qiqishop.dto.response.BaseResponse;
import com.app85soft.qiqishop.dto.response.cart.AddToCartRes;
import com.app85soft.qiqishop.dto.response.cart.CartRes;
import com.app85soft.qiqishop.entities.cart.Cart;
import com.app85soft.qiqishop.entities.cart.CartItem;
import com.app85soft.qiqishop.entities.model.Model;
import com.app85soft.qiqishop.entities.user.User;
import com.app85soft.qiqishop.exceptions.BusinessException;
import com.app85soft.qiqishop.external.GhnClient;
import com.app85soft.qiqishop.repositories.address.AddressRepository;
import com.app85soft.qiqishop.repositories.cart.CartRepository;
import com.app85soft.qiqishop.repositories.cart_item.CartItemRepository;
import com.app85soft.qiqishop.repositories.model.ModelRepository;
import com.app85soft.qiqishop.services.BaseService;
import com.app85soft.qiqishop.services.vnpay.VnpayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl extends BaseService implements CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ModelRepository modelRepository;
    private final AddressRepository addressRepository;
    private final GhnClient ghnClient;
    private final VnpayService vnpayService;

    @Override
    public AddToCartRes addToCart(AddToCartReq addToCartReq) {
        User user = getUser();

        Cart cart = cartRepository.findByUserId(user.getId());
        if (cart == null) {
            cart = new Cart();
            cart.setUserId(user.getId());
            cartRepository.save(cart);
        }

        Model model = modelRepository.findById(addToCartReq.getModelId())
                .orElseThrow(() -> new BusinessException(Translator.toLocale("model_id_not_exist")));

        CartItem cartItem = cartItemRepository.findByCartIdAndModelId(cart.getId(), model.getId());

        int requestedQuantity = addToCartReq.getQuantity();
        int currentQuantity = cartItem != null ? cartItem.getQuantity() : 0;
        int totalQuantity = currentQuantity + requestedQuantity;
        if (model.getStock() < totalQuantity) {
            throw new BusinessException(Translator.toLocale("out_of_stock"));
        }

        if (cartItem == null) {
            cartItem = new CartItem();
            cartItem.setCartId(cart.getId());
            cartItem.setModelId(model.getId());
            cartItem.setQuantity(totalQuantity);
        } else {
            cartItem.setQuantity(totalQuantity);
        }
        cartItemRepository.save(cartItem);

        return AddToCartRes.builder()
                .productId(model.getProductId())
                .modelId(cartItem.getModelId())
                .quantity(cartItem.getQuantity())
                .build();
    }

    @Override
    public BaseResponse<CartRes> updateCart(UpdateCartReq cartReq) {
        User user = getUser();
        Cart cart = cartRepository.findByUserId(user.getId());
        List<CartItem> cartItems = cartItemRepository.findAllByCartId(cart.getId());

        Map<Integer, CartItem> itemMap = cartItems.stream()
                .collect(Collectors.toMap(CartItem::getModelId, Function.identity()));

        CartItem existingItem = itemMap.get(cartReq.getModelId());
        if (existingItem != null) {
            if (cartReq.getQuantity() > 0) {
                existingItem.setQuantity(cartReq.getQuantity());
                cartItemRepository.save(existingItem);
            } else {
                cartItemRepository.delete(existingItem);
            }
        }

        CartRes cartRes = cartRepository.getCartByUserId(user.getId());

        return new BaseResponse<>(cartRes);
    }

    @Override
    public BaseResponse<CartRes> deleteCartItem(IdsRequest req) {
        User user = getUser();
        List<Integer> CartItemIds = req.getIds();
        List<Integer> existingIds = cartRepository.getAllCartIteamIdToCheckExist(CartItemIds, user.getId());
        List<Integer> nonExistingIds = CartItemIds.stream().filter(id -> !existingIds.contains(id)).toList();
        if (!nonExistingIds.isEmpty()) {
            throw new BusinessException(Translator.toLocale("id_not_exist"), HttpStatus.BAD_REQUEST);
        }
        cartItemRepository.deleteAllById(CartItemIds);
        CartRes cartRes = cartRepository.getCartByUserId(user.getId());

        return new BaseResponse<>(cartRes);
    }

    @Override
    public BaseResponse<CartRes> getCart() {
        User user = getUser();
        CartRes cartRes = cartRepository.getCartByUserId(user.getId());

        return new BaseResponse<>(cartRes);
    }

}
