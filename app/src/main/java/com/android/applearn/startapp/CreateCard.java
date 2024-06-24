package com.android.applearn.startapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.android.applearn.R;
import com.android.applearn.addcard.AddCard;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class CreateCard extends AppCompatActivity {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_card);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        viewPager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                switch (position){
                    case 0:
                        return new AddCard();
                    default:
                        return new AddCard();

                }

            }

            @Override
            public int getItemCount() {
                return 1;
            }
        });

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Створити карточку");
                    tab.setContentDescription("Створити карточку");
                    break;
                case 1:
                    tab.setText("Вкладка 2");
                    tab.setContentDescription("Вкладка 2");
                    break;
            }
        }).attach();
    }
}