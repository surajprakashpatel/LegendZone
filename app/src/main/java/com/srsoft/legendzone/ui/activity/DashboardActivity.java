package com.srsoft.legendzone.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.srsoft.legendzone.R;
import com.srsoft.legendzone.databinding.ActivityDashboardBinding;

public class DashboardActivity extends AppCompatActivity {

    private ActivityDashboardBinding binding;
    public NavController bottomNavController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavHostFragment navHost = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.bottom_nav_fragment);
        assert navHost != null;
        bottomNavController = navHost.getNavController();
        NavigationUI.setupWithNavController(binding.bottomNavigation, bottomNavController);

    }

    public void navigateToFragment(int id) {
        bottomNavController.navigate(id);
    }

    public void navigateToFragment(int id, Bundle bundle) {
        bottomNavController.navigate(id, bundle);
    }

    public void removeFromBackStack(int fragmentCard) {
        bottomNavController.popBackStack(fragmentCard, true);
    }
}