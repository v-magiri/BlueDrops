package com.riconets.bluedrop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.riconets.bluedrop.Adapters.CartAdapter;
import com.riconets.bluedrop.model.CartModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Cart#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Cart extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView cartRecyclerView;
    private TextView priceTxt,AmountTv;
    private Button PayNowBtn;
    List<CartModel> cartList;
    CartAdapter cartAdapter;
    DatabaseReference databaseReference;
    RelativeLayout noItemLayout;
    FirebaseAuth mAuth;
    int totalPrice;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Cart() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Cart.
     */
    // TODO: Rename and change types and number of parameters
    public static Cart newInstance(String param1, String param2) {
        Cart fragment = new Cart();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_cart, container, false);
        PayNowBtn=v.findViewById(R.id.payBtn);
        priceTxt=v.findViewById(R.id.TotalPriceTxt);
        AmountTv=v.findViewById(R.id.amountTV);
        cartRecyclerView=v.findViewById(R.id.cartRecyclerView);
        cartList=new ArrayList<>();
        cartRecyclerView.setHasFixedSize(true);
        noItemLayout=v.findViewById(R.id.EmptyCartLayout);
        mAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("Cart");
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        getCartItems();
        return v;
    }

    private void getCartItems() {
        String UID=mAuth.getUid();
        cartList.clear();
        databaseReference.child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    CartModel curCart=dataSnapshot.getValue(CartModel.class);
                    cartList.add(curCart);
                }
                for(int n=0;n<cartList.size();n++){
                    totalPrice+=Integer.parseInt(cartList.get(n).getTotalPrice());
                }
                cartAdapter=new CartAdapter(getActivity(),cartList);
                cartRecyclerView.setAdapter(cartAdapter);
                if(cartList.size()==0){
                    PayNowBtn.setVisibility(View.GONE);
                    priceTxt.setVisibility(View.GONE);
                    AmountTv.setVisibility(View.GONE);
                    noItemLayout.setVisibility(View.VISIBLE);
                }else{
                    PayNowBtn.setVisibility(View.VISIBLE);
                    priceTxt.setVisibility(View.VISIBLE);
                    AmountTv.setVisibility(View.VISIBLE);
                    priceTxt.setText(String.valueOf(totalPrice));
                    noItemLayout.setVisibility(View.GONE);
                }
                cartAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}