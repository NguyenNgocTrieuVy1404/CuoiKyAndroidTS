package com.example.technologyactivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.adapter.CartAdapterItem;
import com.example.adapter.NotificationAdapter;
import com.example.modeldata.CartItem;
import com.example.modeldata.Notification;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CartFragment extends Fragment {

    private ListView cartListView;
    private CartAdapterItem cartAdapter;
    private CartManager cartManager;
    private TextView totalPriceTextView;
    private TextView emptyCartText;
    private ImageView cartImage;
    private Button checkoutButton;
    private List<Notification> notificationList;
    private NotificationAdapter notificationAdapter;

    public CartFragment(List<Notification> notificationList, NotificationAdapter notificationAdapter) {
        this.notificationList = notificationList;
        this.notificationAdapter = notificationAdapter;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cart_fragment, container, false);

        cartListView = view.findViewById(R.id.cartListView);
        totalPriceTextView = view.findViewById(R.id.total_price_text_view);
        checkoutButton = view.findViewById(R.id.btn_checkout);
        emptyCartText = view.findViewById(R.id.empty_cart_text);
        cartImage = view.findViewById(R.id.cart_image);

        cartManager = CartManager.getInstance();

        updateCartItems();

        checkoutButton.setOnClickListener(v -> handleCheckout());

        return view;
    }

    // Cập nhật hiển thị giỏ hàng dựa vào trạng thái
    private void updateCartItems() {
        List<CartItem> cartItems = cartManager.getCartItems();
        if (cartItems.isEmpty()) {
            emptyCartText.setVisibility(View.VISIBLE);
            cartImage.setVisibility(View.VISIBLE);
            cartListView.setVisibility(View.GONE);
            totalPriceTextView.setVisibility(View.GONE);
            checkoutButton.setVisibility(View.GONE);
        } else {
            emptyCartText.setVisibility(View.GONE);
            cartImage.setVisibility(View.GONE);
            cartListView.setVisibility(View.VISIBLE);
            totalPriceTextView.setVisibility(View.VISIBLE);
            checkoutButton.setVisibility(View.VISIBLE);

            cartAdapter = new CartAdapterItem(getActivity(), cartItems, this);
            cartListView.setAdapter(cartAdapter);

            updateTotalPrice();
        }
    }


    public void updateTotalPrice() {
        double totalPrice = calculateTotalPrice(cartAdapter.getSelectedItems());
        totalPriceTextView.setText("Tổng tiền: " + String.format(Locale.getDefault(), "%.2f", totalPrice));
    }

    private double calculateTotalPrice(List<CartItem> selectedItems) {
        double totalPrice = 0;
        for (CartItem item : selectedItems) {
            totalPrice += item.getProduct().getPrice() * item.getQuantity();
        }
        return totalPrice;
    }

    private void handleCheckout() {
        List<CartItem> selectedItems = cartAdapter.getSelectedItems();
        if (selectedItems.isEmpty()) {
            Toast.makeText(getActivity(), "Vui lòng chọn sản phẩm để thanh toán!", Toast.LENGTH_SHORT).show();
        } else {
            double totalPrice = calculateTotalPrice(selectedItems);
            showConfirmationDialog(totalPrice, selectedItems);
        }
    }

    private void showConfirmationDialog(double totalPrice, List<CartItem> selectedItems) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View customView = inflater.inflate(R.layout.custom_alert_dialog, null);

        TextView tvMessage = customView.findViewById(R.id.tvMessage);
        tvMessage.setText("Tổng số tiền: " + totalPrice + "\nBạn có muốn tiếp tục thanh toán không?");

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(customView)
                .create();

        Button btnCancel = customView.findViewById(R.id.btnCancel);
        Button btnConfirm = customView.findViewById(R.id.btnConfirm);

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        btnConfirm.setOnClickListener(v -> {
            processPayment(totalPrice, selectedItems);
            dialog.dismiss();
        });

        dialog.show();
    }


    private void processPayment(double totalPrice, List<CartItem> selectedItems) {
        boolean paymentSuccess = true;

        if (paymentSuccess) {
            Toast.makeText(getActivity(), "Thanh toán thành công! Số tiền: " + totalPrice, Toast.LENGTH_SHORT).show();
            cartManager.removeItems(selectedItems);
            updateCartItems();

            String productNames = getProductNames(selectedItems);
            String currentTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());

            String content = "Bạn đã thanh toán thành công sản phẩm: " + productNames + "\n" +
                    "Với số tiền là " + totalPrice + "\n" +
                    "Vào lúc " + currentTime + ".\n" +
                    "Cảm ơn bạn đã ủng hộ!";
            createNotification("TV. STORE", content);
        } else {
            Toast.makeText(getActivity(), "Thanh toán thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
        }
    }


    private String getProductNames(List<CartItem> selectedItems) {
        StringBuilder productNames = new StringBuilder();
        for (CartItem item : selectedItems) {
            productNames.append(item.getProduct().getName()).append(", ");
        }
        if (productNames.length() > 0) {
            productNames.setLength(productNames.length() - 2);
        }
        return productNames.toString();
    }

    private void createNotification(String title, String content) {
        Notification notification = new Notification(title, content);
        notificationList.add(notification);
        notificationAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateCartItems();
    }
}
