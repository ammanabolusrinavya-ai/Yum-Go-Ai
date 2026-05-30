package com.example.viewmodel

import androidx.lifecycle.ViewModel
import com.example.models.MenuItem
import com.example.models.Restaurant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class CartItem(
    val menuItem: MenuItem,
    val quantity: Int
)

data class CartState(
    val currentRestaurantId: String? = null,
    val items: Map<String, CartItem> = emptyMap() // Map of menuItemId to CartItem
) {
    val subtotal: Double
        get() = items.values.sumOf { it.menuItem.price * it.quantity }
}

class CartViewModel : ViewModel() {
    private val _cartState = MutableStateFlow(CartState())
    val cartState: StateFlow<CartState> = _cartState.asStateFlow()

    private val _crossRestaurantAttempt = MutableStateFlow<CartItem?>(null) // Contains conflicting item and restaurant ID
    val crossRestaurantAttempt = _crossRestaurantAttempt.asStateFlow()

    fun addToCart(menuItem: MenuItem, restaurantId: String) {
        val currentState = _cartState.value
        
        // Check single restaurant rule
        if (currentState.currentRestaurantId != null && currentState.currentRestaurantId != restaurantId) {
            _crossRestaurantAttempt.value = CartItem(menuItem, 1) // Store the item to be added
            return
        }

        _cartState.update { state ->
            val updatedItems = state.items.toMutableMap()
            val existing = updatedItems[menuItem.id]
            if (existing != null) {
                updatedItems[menuItem.id] = existing.copy(quantity = existing.quantity + 1)
            } else {
                updatedItems[menuItem.id] = CartItem(menuItem, 1)
            }
            state.copy(
                currentRestaurantId = restaurantId,
                items = updatedItems
            )
        }
    }

    fun removeFromCart(menuItemId: String) {
        _cartState.update { state ->
            val updatedItems = state.items.toMutableMap()
            val existing = updatedItems[menuItemId]
            if (existing != null) {
                if (existing.quantity > 1) {
                    updatedItems[menuItemId] = existing.copy(quantity = existing.quantity - 1)
                } else {
                    updatedItems.remove(menuItemId)
                }
            }
            
            // If cart is empty, clear the current restaurant lock
            val newRestaurantId = if (updatedItems.isEmpty()) null else state.currentRestaurantId
            
            state.copy(
                currentRestaurantId = newRestaurantId,
                items = updatedItems
            )
        }
    }

    fun clearCartAndAdd() {
        val attempt = _crossRestaurantAttempt.value
        if (attempt != null) {
            _cartState.value = CartState(
                currentRestaurantId = attempt.menuItem.restaurantId,
                items = mapOf(attempt.menuItem.id to attempt)
            )
        } else {
            _cartState.value = CartState()
        }
        clearConflict()
    }

    fun clearConflict() {
        _crossRestaurantAttempt.value = null
    }

    fun clearCart() {
        _cartState.value = CartState()
    }
}
