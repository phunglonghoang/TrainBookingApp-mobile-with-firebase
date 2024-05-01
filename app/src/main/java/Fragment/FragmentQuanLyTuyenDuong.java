package Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;
import com.tvc.datvetaumobileapp.R;

public class FragmentQuanLyTuyenDuong extends Fragment {
    private View rootView;
    private FrameLayout frameLayout;
    private TabLayout tabLayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_quanlytuyenduong, container, false);
        init();

        setFragment(new FragmentQLTuyenDuong());
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = null;
                switch (tab.getPosition()){
                    case 0:
                        fragment = new FragmentQLTuyenDuong();
                        break;
                    case 1:
                        fragment = new FragmentThemTuyenDuong();
                        break;
                    case 2:
                        fragment = new FragmentThemGaTau();
                        break;
                }
                setFragment(fragment);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return rootView;
    }
    private void init(){
        frameLayout = rootView.findViewById(R.id.frameLayout_QLTuyenDuong);
        tabLayout = rootView.findViewById(R.id.tabLayout_QLTuyenDuong);
    }
    private void setFragment(Fragment fragment){
        if (fragment != null) {
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayout_QLTuyenDuong, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}
