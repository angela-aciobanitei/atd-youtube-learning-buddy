package com.ang.acb.youtubelearningbuddy.utils;

import android.content.Intent;
import android.util.SparseArray;
import android.view.MenuItem;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.fragment.NavHostFragment;

import com.ang.acb.youtubelearningbuddy.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;
;
import java.util.List;

public class NavigationUtils {

    @NotNull
    public static LiveData<NavController> setupWithNavController(
                                            @NotNull final BottomNavigationView bottomNavigationView,
                                            @NotNull List<Integer> navGraphIds,
                                            @NotNull final FragmentManager fragmentManager,
                                            int containerId, @NotNull Intent intent) {
        // Map of tags
        SparseArray<String> graphIdToTagMap = new SparseArray<>();

        // Result is a mutable live data with the selected nav controller
        MutableLiveData<NavController> selectedNavController = new MutableLiveData<>();

        int firstFragmentGraphId = 0;

        // First create a NavHostFragment for each NavGraph ID
        int i = 0;
        for (Integer navGraphId : navGraphIds) {
            int index = i++;
            String fragmentTag = getFragmentTag(index);

            // Find or create the Navigation host fragment
            NavHostFragment navHostFragment = obtainNavHostFragment(
                    fragmentManager, fragmentTag, navGraphId, containerId);

            // Obtain its ID
            int graphId = navHostFragment.getNavController().getGraph().getId();

            if (index == 0) firstFragmentGraphId = graphId;

            // Save to the map
            graphIdToTagMap.put(graphId, fragmentTag);

            // Attach or detach nav host fragment depending on whether it's the selected item.
            if (bottomNavigationView.getSelectedItemId() == graphId) {
                // Update live data with the selected graph
                selectedNavController.setValue(navHostFragment.getNavController());
                attachNavHostFragment(fragmentManager, navHostFragment, index == 0);
            } else {
                detachNavHostFragment(fragmentManager, navHostFragment);
            }
        }

        // Now connect selecting an item with swapping Fragments
        final String selectedItemTag = graphIdToTagMap.get(bottomNavigationView.getSelectedItemId());
        final String firstFragmentTag = graphIdToTagMap.get(firstFragmentGraphId);
        final boolean isOnFirstFragment = Objects.equals(selectedItemTag, firstFragmentTag);

        // When a navigation item is selected
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            boolean displayItem;
            if (fragmentManager.isStateSaved()) {
                displayItem = false;
            } else {
                String newlySelectedItemTag = graphIdToTagMap.get(item.getItemId());
                if (!Objects.equals(selectedItemTag, newlySelectedItemTag)) {
                    // Pop everything above the first fragment (the "fixed start destination")
                    fragmentManager.popBackStack(firstFragmentTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    Fragment fragmentByTag = fragmentManager.findFragmentByTag(newlySelectedItemTag);
                    if (fragmentByTag == null) {
                        throw new ClassCastException("null cannot be cast to NavHostFragment");
                    }
                    NavHostFragment selectedFragment = (NavHostFragment) fragmentByTag;

                    // Exclude the first fragment tag because it's always in the back stack.
                    if (!Objects.equals(firstFragmentTag, newlySelectedItemTag)) {
                        // Commit a transaction that cleans the back stack and adds the first fragment
                        // to it, creating the fixed started destination.
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                                .attach(selectedFragment)
                                .setPrimaryNavigationFragment(selectedFragment);

                        // Detach all other Fragments
                        int index = 0;
                        for(int graphSize = graphIdToTagMap.size(); index < graphSize; ++index) {
                            String fragmentTagIter = graphIdToTagMap.valueAt(index);
                            if (!Objects.equals(fragmentTagIter, newlySelectedItemTag)) {
                                Fragment firstFragment = fragmentManager.findFragmentByTag(firstFragmentTag);
                                if (firstFragment != null) fragmentTransaction.detach(firstFragment);
                            }
                        }

                        fragmentTransaction
                                .addToBackStack(firstFragmentTag)
                                .setCustomAnimations(
                                        R.anim.nav_default_enter_anim,
                                        R.anim.nav_default_exit_anim,
                                        R.anim.nav_default_pop_enter_anim,
                                        R.anim.nav_default_pop_exit_anim)
                                .setReorderingAllowed(true)
                                .commit();
                    }

                    selectedItemTag = newlySelectedItemTag;
                    isOnFirstFragment = Objects.equals(selectedItemTag, firstFragmentTag);
                    selectedNavController.setValue(selectedFragment.getNavController());
                    displayItem = true;
                } else {
                    displayItem = false;
                }
            }

            return displayItem;
        });

        // Optional: on item reselected, pop back stack to the destination of the graph
        setupItemReselected(bottomNavigationView, graphIdToTagMap, fragmentManager);

        // Handle deep link
        setupDeepLinks(bottomNavigationView, navGraphIds, fragmentManager, containerId, intent);

        // Finally, ensure that we update our BottomNavigationView when the back stack changes
        fragmentManager.addOnBackStackChangedListener((FragmentManager.OnBackStackChangedListener)
                (new FragmentManager.OnBackStackChangedListener() {
            public final void onBackStackChanged() {
                if (!isOnFirstFragment && !isOnBackStack(fragmentManager, firstFragmentTag)) {
                    bottomNavigationView.setSelectedItemId(firstFragmentGraphId);
                }

                // Reset the graph if the currentDestination is not valid (happens
                // when the back stack is popped after using the back button).
                NavController controller = (NavController) selectedNavController.getValue();
                if (controller != null) {
                    if (controller.getCurrentDestination() == null) {
                        controller.navigate(controller.getGraph().getId());
                    }
                }
            }
        }));
        return selectedNavController;
    }

    private static void setupDeepLinks(@NotNull BottomNavigationView bottomNavigationView,
                                             List<Integer> navGraphIds, FragmentManager fragmentManager,
                                             int containerId, Intent intent) {
        int i = 0;
        for (Integer navGraphId : navGraphIds) {
            int index = i++;
            String fragmentTag = getFragmentTag(index);

            // Find or create the Navigation host fragment
            NavHostFragment navHostFragment = obtainNavHostFragment(
                    fragmentManager, fragmentTag, navGraphId, containerId);

            // Handle Intent
            if (navHostFragment.getNavController().handleDeepLink(intent)) {
                int selectedItemId = bottomNavigationView.getSelectedItemId();
                NavController navController = navHostFragment.getNavController();
                NavGraph navGraph = navController.getGraph();
                if (selectedItemId != navGraph.getId()) {
                    navController = navHostFragment.getNavController();
                    navGraph = navController.getGraph();
                    bottomNavigationView.setSelectedItemId(navGraph.getId());
                }
            }
        }
    }

    private static void setupItemReselected(@NotNull BottomNavigationView bottomNavigationView,
                                            final SparseArray graphIdToTagMap,
                                            final FragmentManager fragmentManager) {
        bottomNavigationView.setOnNavigationItemReselectedListener(
                new BottomNavigationView.OnNavigationItemReselectedListener() {
                public final void onNavigationItemReselected(@NotNull MenuItem item) {
                    String newlySelectedItemTag = (String)graphIdToTagMap.get(item.getItemId());
                    Fragment fragmentByTag = fragmentManager.findFragmentByTag(newlySelectedItemTag);
                    if (fragmentByTag == null) {
                        throw new ClassCastException("null cannot be cast to NavHostFragment");
                    } else {
                        NavHostFragment selectedFragment = (NavHostFragment) fragmentByTag;
                        NavController navController = selectedFragment.getNavController();
                        NavGraph navGraph = navController.getGraph();
                        // Pop the back stack to the start destination of the current navController graph
                        navController.popBackStack(navGraph.getStartDestination(), false);
                }
            }
        });
    }

    private static void detachNavHostFragment(FragmentManager fragmentManager,
                                              NavHostFragment navHostFragment) {
        fragmentManager.beginTransaction()
                .detach((Fragment)navHostFragment)
                .commitNow();
    }

    private static void attachNavHostFragment(FragmentManager fragmentManager,
                                              NavHostFragment navHostFragment,
                                              boolean isPrimaryNavFragment) {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                .attach((Fragment)navHostFragment);

        if (isPrimaryNavFragment) {
            fragmentTransaction.setPrimaryNavigationFragment((Fragment)navHostFragment);
        }

        fragmentTransaction.commitNow();
    }

    private static NavHostFragment obtainNavHostFragment(FragmentManager fragmentManager,
                                                         String fragmentTag, int navGraphId,
                                                         int containerId) {
        // If the Nav Host fragment exists, return it
        NavHostFragment existingFragment = (NavHostFragment)
                fragmentManager.findFragmentByTag(fragmentTag);

        // Otherwise, create it and return it.
        if (existingFragment != null) {
            return existingFragment;
        } else {
            NavHostFragment navHostFragment = NavHostFragment.create(navGraphId);
            fragmentManager.beginTransaction()
                    .add(containerId, navHostFragment, fragmentTag)
                    .commitNow();
            return navHostFragment;
        }
    }

    private static boolean isOnBackStack(@NotNull FragmentManager fragmentManager, String backStackName) {
        int backStackCount = fragmentManager.getBackStackEntryCount();
        int index = 0;

        for(; index < backStackCount; ++index) {
            if (Objects.equals(fragmentManager.getBackStackEntryAt(index).getName(), backStackName)) {
                return true;
            }
        }
        return false;
    }

    private static String getFragmentTag(int index) {
        return "bottomNavigation#" + index;
    }
}
