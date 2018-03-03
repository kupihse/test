package com.example.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.adapters.SearchItemsAdapter;
import com.example.application.R;
import com.example.models.Product;
import com.example.services.Services;
import com.example.util.OnSwipeTouchListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    private SearchItemsAdapter productAdapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;


    private String searchText;

    private View userHint;
    private SearchView searchView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // На потом, надо сделать обновление по свайпу вниз
        //
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setDistanceToTriggerSync(250);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                rerenderSearch();
            }
        });
        userHint = view.findViewById(R.id.user_hint);

        userHint.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
            @Override
            public void onSwipeBottom() {

                if (searchView != null) {
                    searchView.setIconified(true);
                    searchView.setIconified(false);
                }

                super.onSwipeBottom();
            }
        });

        setRecyclerViewLayout(view);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search_fragment, menu);

        searchView = (SearchView) menu.findItem(R.id.menu_search_fragment_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchText = s;
                rerenderSearch();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        searchView.onActionViewExpanded();
        searchView.clearFocus();
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onStop() {

        if (searchView != null) {
            searchView.clearFocus();
        }

        super.onStop();
    }


    private void setRecyclerViewLayout(View root) {
        productAdapter = new SearchItemsAdapter();

//        productAdapter.setOnItemLongClickListener(new ScrollingItemsAdapter.OnItemLongClickListener() {
//            @Override
//            public void onItemLongClick(Product product) {
//                getFragmentManager().beginTransaction()
//                        .add(R.id.scrolling_activity_layout, new PreviewTestFragment())
//                        .addToBackStack(null)
//                        .commit();
//            }
//        });

        recyclerView = root.findViewById(R.id.products);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(productAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // Обновляем только при отсутствии скролла (долистали до самого верха и листаем еще – тогда норм)
                swipeRefreshLayout.setEnabled(newState == RecyclerView.SCROLL_STATE_IDLE);
            }
        });
//        toolbar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                recyclerView.scrollToPosition(0);
//            }
//        });
    }

    private void rerenderSearch() {
        if (searchText == null || searchText.equals("")) {
            return;
        }
        swipeRefreshLayout.setEnabled(true);
        // делаем запрос на все товары
        Services.search.searchByName(searchText).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                List<Product> prs = response.body();

                // Если ничего не пришло, то ничего не делаем
                if (prs == null || prs.size() == 0) {
                    return;
                }

                userHint.setVisibility(View.GONE);
                swipeRefreshLayout.setVisibility(View.VISIBLE);

                // Если что-то есть закидываем это в массив
                productAdapter.setProducts(prs);

                swipeRefreshLayout.setEnabled(false);
            }

            // Если чет все плохо, то просто пишем в лог, пока что
            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.d("RERENDER FAIL", t.toString());
            }
        });
    }
}
