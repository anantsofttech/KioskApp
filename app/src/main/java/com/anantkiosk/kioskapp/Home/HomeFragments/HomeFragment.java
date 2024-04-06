package com.anantkiosk.kioskapp.Home.HomeFragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.anantkiosk.kioskapp.MainActivity;
import com.anantkiosk.kioskapp.Utils.UtilsGlobal;
import com.anantkiosk.kioskapp.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    public static boolean isDetailUp = false;
    public int seltype = -1;

    public static HomeFragment aactivity;

    boolean isLoyalty = true, isGiftCard = true, isRecentPurhase = true, isFoodDrink = false, isDrink = false;
    int countForTest = 6;
    String[] permissions_camera = new String[]{
            Manifest.permission.CAMERA};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        aactivity = this;
        HomeFragment.aactivity.seltype=-1;
        binding.txtloyalty.setTypeface(UtilsGlobal.setFontSemiBold(getActivity()));
        binding.txtgiftcard.setTypeface(UtilsGlobal.setFontSemiBold(getActivity()));
        binding.txtitemlookup.setTypeface(UtilsGlobal.setFontSemiBold(getActivity()));
        binding.txtpurchasehistory.setTypeface(UtilsGlobal.setFontSemiBold(getActivity()));
        binding.txtdrink.setTypeface(UtilsGlobal.setFontSemiBold(getActivity()));
        binding.txtdrink1.setTypeface(UtilsGlobal.setFontSemiBold(getActivity()));
        binding.txtfood.setTypeface(UtilsGlobal.setFontSemiBold(getActivity()));
        binding.txtfood1.setTypeface(UtilsGlobal.setFontSemiBold(getActivity()));
        binding.txtpurchasehistory.setTypeface(UtilsGlobal.setFontSemiBold(getActivity()));
        try {
            if (!UtilsGlobal.store.isLoyaltyEnable()) {
                binding.cardLoyalty.setVisibility(View.GONE);
                countForTest--;
                isLoyalty = false;
            } else {
                if (UtilsGlobal.store.getIsLoyaltyPointAllowCheckPoint() == null) {
                    isLoyalty = false;
                    countForTest--;
                    binding.cardLoyalty.setVisibility(View.GONE);
                } else if (UtilsGlobal.store.getIsLoyaltyPointAllowCheckPoint().trim().length() <= 0) {
                    isLoyalty = false;
                    countForTest--;
                    binding.cardLoyalty.setVisibility(View.GONE);
                } else if (UtilsGlobal.store.getIsLoyaltyPointAllowCheckPoint().trim().equalsIgnoreCase("false")) {
                    isLoyalty = false;
                    countForTest--;
                    binding.cardLoyalty.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
        }
        try {
            if (UtilsGlobal.store.getIsLoyaltyPointAllowAllCustomers() == null) {
                isRecentPurhase = false;
                countForTest--;
                binding.cardPurchaseHistory.setVisibility(View.GONE);
            } else if (UtilsGlobal.store.getIsLoyaltyPointAllowAllCustomers().trim().length() <= 0) {
                isRecentPurhase = false;
                countForTest--;
                binding.cardPurchaseHistory.setVisibility(View.GONE);
            } else if (UtilsGlobal.store.getIsLoyaltyPointAllowAllCustomers().trim().equalsIgnoreCase("false")) {
                isRecentPurhase = false;
                countForTest--;
                binding.cardPurchaseHistory.setVisibility(View.GONE);
            }

        } catch (Exception e) {
        }
        try {
            if (!UtilsGlobal.store.isGiftCardEnable()) {
                isGiftCard = false;
                countForTest--;
                binding.cardGiftcards.setVisibility(View.GONE);
            } else {
                if (UtilsGlobal.store.getIsAllowGiftCardBalanceCheck() == null) {
                    isGiftCard = false;
                    countForTest--;
                    binding.cardGiftcards.setVisibility(View.GONE);
                } else if (UtilsGlobal.store.getIsAllowGiftCardBalanceCheck().trim().length() <= 0) {
                    isGiftCard = false;
                    countForTest--;
                    binding.cardGiftcards.setVisibility(View.GONE);
                } else if (UtilsGlobal.store.getIsAllowGiftCardBalanceCheck().trim().equalsIgnoreCase("false")) {
                    isGiftCard = false;
                    countForTest--;
                    binding.cardGiftcards.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
        }
        if (UtilsGlobal.store.isHasWineSpirit()) {
            if (UtilsGlobal.store.isHasFoodPairing()) {
                binding.cardFoodpair.setVisibility(View.VISIBLE);
                binding.cardFoodpair1.setVisibility(View.VISIBLE);
                isFoodDrink = true;
            } else {
                isFoodDrink = false;
                countForTest--;
                binding.cardFoodpair.setVisibility(View.GONE);
                binding.cardFoodpair1.setVisibility(View.GONE);
            }
            if (UtilsGlobal.store.isHasDrinkReceipes()) {
                isDrink = true;
                binding.carddrink.setVisibility(View.VISIBLE);
                binding.carddrink2.setVisibility(View.VISIBLE);
            } else {
                isDrink = false;
                countForTest--;
                binding.carddrink.setVisibility(View.GONE);
                binding.carddrink2.setVisibility(View.GONE);
            }
        } else {
            isFoodDrink = false;
            isDrink = false;
            countForTest--;
            countForTest--;
            binding.cardFoodpair.setVisibility(View.GONE);
            binding.cardFoodpair1.setVisibility(View.GONE);
            binding.carddrink.setVisibility(View.GONE);
            binding.carddrink2.setVisibility(View.GONE);
        }
//        Edited by Varun for when only 4 option is onn from the set-up wizard side then Screen doesn't not appear good in home screen
        if (countForTest >= 4) {
//        if (countForTest >= 5) {
//            END
            rearrangeDashboard(true, countForTest);
        } else {
            rearrangeDashboard(false, countForTest);
        }
        binding.cardLoyalty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open
                try {
                    if (UtilsGlobal.store.isLoyaltyEnable()) {
                        if (UtilsGlobal.store.getIsLoyaltyPointAllowCheckPoint() == null) {
                        } else if (UtilsGlobal.store.getIsLoyaltyPointAllowCheckPoint().trim().length() <= 0) {
                        } else if (UtilsGlobal.store.getIsLoyaltyPointAllowCheckPoint().trim().equalsIgnoreCase("false")) {
                        } else {
                            if (UtilsGlobal.getCustomerList(getActivity()) == null) {
                                LoyaltyRewardsFragment homeFragment = new LoyaltyRewardsFragment();
                                MainActivity.contex.changeFragment(homeFragment);
                            } else if (UtilsGlobal.getCustomerList(getActivity()).getId() == null) {
                                LoyaltyRewardsFragment homeFragment = new LoyaltyRewardsFragment();
                                MainActivity.contex.changeFragment(homeFragment);
                            } else if (UtilsGlobal.getCustomerList(getActivity()).getId().trim().length() <= 0) {
                                LoyaltyRewardsFragment homeFragment = new LoyaltyRewardsFragment();
                                MainActivity.contex.changeFragment(homeFragment);
                            }
                        }
                    } else {
                    }
                } catch (Exception e) {
                    LoyaltyRewardsFragment homeFragment = new LoyaltyRewardsFragment();
                    MainActivity.contex.changeFragment(homeFragment);
                }

            }
        });
        binding.cardItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seltype = 2;
                ItemLookupFragment homeFragment = new ItemLookupFragment();
                MainActivity.contex.changeFragment(homeFragment);

            }
        });
        binding.cardGiftcards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open
                seltype = 1;
                GiftCardFragment homeFragment = new GiftCardFragment();
                MainActivity.contex.changeFragment(homeFragment);
            }
        });
        binding.cardPurchaseHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open
                seltype = 3;
                RecentPurchaseFragment homeFragment = new RecentPurchaseFragment();
                MainActivity.contex.changeFragment(homeFragment);

            }
        });
        binding.cardFoodpair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open
                seltype = 4;
                FoodPairingFragment homeFragment = new FoodPairingFragment();
                MainActivity.contex.changeFragment(homeFragment);

            }
        });
        binding.carddrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open
                seltype = 5;
                DrinkReceipesFragment homeFragment = new DrinkReceipesFragment();
                MainActivity.contex.changeFragment(homeFragment);

            }
        });
        binding.cardFoodpair1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open
                seltype = 4;
                FoodPairingFragment homeFragment = new FoodPairingFragment();
                MainActivity.contex.changeFragment(homeFragment);

            }
        });
        binding.carddrink2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open
                seltype = 5;
                DrinkReceipesFragment homeFragment = new DrinkReceipesFragment();
                MainActivity.contex.changeFragment(homeFragment);

            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public boolean checkPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null &&
                permissions != null) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(context, permission) !=
                        PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void turnOnPermissions(int code) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                    permissions_camera,
                    code
            );
        } else {
            Toast.makeText(getActivity(), "Please turn on camera permission from setting", Toast.LENGTH_SHORT).show();
        }
    }

    public void rearrangeDashboard(boolean isGrid, int countForTest) {
        {
            binding.llgrid.removeAllViews();
            binding.lllast.setVisibility(View.GONE);
            int countForFive = 1;
            if (isGrid) {
                binding.llgrid.setColumnCount(2);
            } else {
                binding.llgrid.setColumnCount(1);
            }
            if (isLoyalty) {
                countForFive++;
                binding.llgrid.addView(binding.cardLoyalty);
            }
            if (isGiftCard) {
                countForFive++;
                binding.llgrid.addView(binding.cardGiftcards);
            }
            binding.llgrid.addView(binding.cardItems);
            if (isRecentPurhase) {
                countForFive++;
                binding.llgrid.addView(binding.cardPurchaseHistory);
            }
            if (isFoodDrink) {
                countForFive++;
                if (countForFive == 5 && countForTest == 5) {
                    binding.lllast.setVisibility(View.VISIBLE);
                    binding.cardFoodpair.setVisibility(View.GONE);
                    binding.cardFoodpair1.setVisibility(View.VISIBLE);
                    binding.carddrink2.setVisibility(View.GONE);
                } else {
                    binding.llgrid.addView(binding.cardFoodpair);
                }

            }
            if (isDrink) {
                countForFive++;
                if (countForFive == 5 && countForTest == 5) {
                    binding.lllast.setVisibility(View.VISIBLE);
                    binding.carddrink.setVisibility(View.GONE);
                    binding.cardFoodpair1.setVisibility(View.GONE);
                    binding.carddrink2.setVisibility(View.VISIBLE);
                } else {
                    binding.llgrid.addView(binding.carddrink);
                }
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        countForTest=6;
    }

}