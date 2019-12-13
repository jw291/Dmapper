package com.example.dmapper.BottomBarFragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dmapper.R;

import java.util.ArrayList;

class RecyclerviewProduct {
    private String name;

    public RecyclerviewProduct(String name) {
        this.name = name;
    }

    public String getName() { return this.name; }
}

class RecyclerviewViewHolder extends RecyclerView.ViewHolder {

    public TextView textView;

    public RecyclerviewViewHolder(View itemView) {
        super(itemView);

        textView = (TextView) itemView.findViewById(R.id.itemtextview);
    }
}
class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewViewHolder> implements View.OnCreateContextMenuListener{

    ArrayList<RecyclerviewProduct> arrayList;
    Context context;
    RecyclerviewViewHolder holder;
    public int itemposition;
    public void setArrayList(ArrayList<RecyclerviewProduct> list, Context context) {
        this.arrayList = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        holder = new RecyclerviewViewHolder(view);
        view.setOnCreateContextMenuListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerviewViewHolder holder, int position) {

        RecyclerviewProduct data = arrayList.get(holder.getAdapterPosition());
        System.out.println("qqqqqqqqqqq"+position);
        holder.textView.setText(data.getName());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                itemposition = holder.getAdapterPosition();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuItem update = menu.add(Menu.NONE,100,1,"수정");
        MenuItem remove = menu.add(Menu.NONE,101,2,"삭제");

        update.setOnMenuItemClickListener(onEditMenu);
        remove.setOnMenuItemClickListener(onEditMenu);
    }

    private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()){
                case 100 :
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    View view = LayoutInflater.from(context).inflate(R.layout.dialogupdate,null,false);
                    builder.setView(view);

                    final Button buttonSubmit = (Button) view.findViewById(R.id.dialog_friendupdate_btn);
                    final EditText editupdateText = (EditText) view.findViewById(R.id.editupdatename);

                    editupdateText.setText(arrayList.get(itemposition).getName());

                    final AlertDialog dialog = builder.create();
                    buttonSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String updatename = editupdateText.getText().toString();

                            arrayList.set(itemposition,new RecyclerviewProduct(updatename));

                            notifyItemChanged(itemposition);

                            Toast.makeText(holder.itemView.getContext(), "수정 완료", Toast.LENGTH_SHORT).show();

                            dialog.dismiss();
                        }
                    });
                    dialog.show();

                    break;

                case 101 :

                    arrayList.remove(itemposition);
                    notifyItemRemoved(itemposition);
                    notifyItemRangeChanged(itemposition,arrayList.size());
                    Toast.makeText(holder.itemView.getContext(), "삭제 완료", Toast.LENGTH_SHORT).show();

                    break;
            }

            return true;
        }
    };
}
public class lookupdatatab extends Fragment {
    RecyclerView recyclerView;
    RecyclerviewAdapter adapter;
    ArrayList<RecyclerviewProduct> products;
    LinearLayoutManager mLayoutManager;

    com.github.clans.fab.FloatingActionButton itemaddbutton;
    Dialog dialog;

    public lookupdatatab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lookupdatatab, container, false);
        itemaddbutton = (com.github.clans.fab.FloatingActionButton) view.findViewById(R.id.floatingbutton);

        recyclerView = (RecyclerView) view.findViewById(R.id.recy);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        products = new ArrayList<>();
        adapter = new RecyclerviewAdapter();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        itemaddbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.dialogadd);
                System.out.println("진입");
                final EditText editText = (EditText) dialog.findViewById(R.id.edittext);
                Button friendaddbt = (Button) dialog.findViewById(R.id.dialog_friendadd_btn);
                friendaddbt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = editText.getText().toString();
                        products.add(new RecyclerviewProduct(name));
                        adapter.notifyDataSetChanged();
                        adapter.setArrayList(products, getContext());
                        recyclerView.setAdapter(adapter);

                        Toast.makeText(getContext(), "추가되었습니다.", Toast.LENGTH_SHORT).show();

                        dialog.dismiss();
                    }
                });
                createdialog(dialog);
            }
        });
    }
    public void createdialog(Dialog dialog){
        dialog.show();

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        Window window = dialog.getWindow();

        int x = (int)(size.x * 0.8f);
        int y = (int)(size.y * 0.25f);

        window.setLayout(x,y);
    }

}
