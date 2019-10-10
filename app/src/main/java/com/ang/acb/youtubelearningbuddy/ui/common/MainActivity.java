package com.ang.acb.youtubelearningbuddy.ui.common;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.ang.acb.youtubelearningbuddy.R;
import com.ang.acb.youtubelearningbuddy.databinding.ActivityMainBinding;
import com.ang.acb.youtubelearningbuddy.utils.NavigationUtils;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;


/**
 * An activity that inflates a layout that has a NavHostFragment.
 */
public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    private ActivityMainBinding binding;

    private LiveData<NavController> currentNavController;

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        // Note: a DispatchingAndroidInjector<T> performs members-injection
        // on instances of core Android types (e.g. Activity, Fragment) that
        // are constructed by the Android framework and not by Dagger.
        return dispatchingAndroidInjector;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Note: when using Dagger for injecting Activity
        // objects, inject as early as possible.
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        // Inflate view and obtain an instance of the binding class.
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // Specify the current activity as the lifecycle owner.
        binding.setLifecycleOwner(this);

        if (savedInstanceState == null) {
            setupBottomNavigationBar();
        } // Else, need to wait for onRestoreInstanceState
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Now that BottomNavigationBar has restored its instance state
        // and its selectedItemId, we can proceed with setting up the
        // BottomNavigationBar with Navigation
        setupBottomNavigationBar();
    }

    private void setupBottomNavigationBar() {
        List<Integer> navGraphIds = Arrays.asList(
                R.navigation.search,
                R.navigation.topics,
                R.navigation.favorites);


        LiveData<NavController> controller = NavigationUtils.setupWithNavController(
                binding.mainBottomNavigation,
                navGraphIds,
                getSupportFragmentManager(),
                R.id.main_fragment_container,
                getIntent());

        // Whenever the selected controller changes, setup the action bar.
        controller.observe(this, new Observer<NavController>() {
            @Override
            public void onChanged(NavController navController) {
                NavigationUI.setupActionBarWithNavController(MainActivity.this, navController);
            }
        });

        currentNavController = controller;
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Returns true if Up navigation completed successfully
        // and this Activity was finished, false otherwise.
        if (currentNavController != null) {
            NavController controller = currentNavController.getValue();
            if (controller != null) {
                return controller.navigateUp();
            }
        }
        return false;
    }
}