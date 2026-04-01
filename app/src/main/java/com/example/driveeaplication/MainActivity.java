package com.example.driveeaplication;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.driveeaplication.databinding.ActivityMainBinding;
import com.example.driveeaplication.models.User;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import com.example.driveeaplication.viewmodels.MainViewModel;
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MainViewModel viewModel;
    private void showOrderDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.layout_order_sheet, null);
        dialog.setContentView(view);
        EditText etPrice = view.findViewById(R.id.etPrice);
        Button btnConfirm = view.findViewById(R.id.btnConfirmOrder);
        CheckBox cbPoints = view.findViewById(R.id.cbUsePoints);
        btnConfirm.setOnClickListener(v -> {
            String priceStr = etPrice.getText().toString().trim();
            if (priceStr.isEmpty() && !cbPoints.isChecked()) {
                Toast.makeText(this, "Введите цену!", Toast.LENGTH_SHORT).show();
                return;
            }
            int price = priceStr.isEmpty() ? 0 : Integer.parseInt(priceStr);
            User user = viewModel.getUserData().getValue();
            if (user == null) return;
            boolean success = viewModel.processTrip(price, cbPoints.isChecked(), true, user.getDriver());
            if (success) {
                Toast.makeText(this, "Готово!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                String errorMsg = cbPoints.isChecked() ? "Мало баллов!" : "Ошибка в цене!";
                Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        binding.mapView.setBuiltInZoomControls(false);
        binding.mapView.setTileSource(TileSourceFactory.MAPNIK);
        binding.mapView.setMultiTouchControls(true);

        GeoPoint startPoint = new GeoPoint(62.03, 129.73);
        binding.mapView.getController().setZoom(15.0);
        binding.mapView.getController().setCenter(startPoint);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewModel.init();
        viewModel.getUserData().observe(this, user -> {
            binding.tvPoints.setText("⭐ " + user.getPoints() + " (" + user.getStatus() + ")");
        });
        binding.btnSearch.setOnClickListener(v -> {
            showOrderDialog();
        });
        binding.pointsCard.setOnClickListener(v -> {
            User user = viewModel.getUserData().getValue();
            if (user != null) {
                Toast.makeText(this, "У вас " + user.getPoints() + " баллов Drivee", Toast.LENGTH_SHORT).show();
            }
        });


    }
}