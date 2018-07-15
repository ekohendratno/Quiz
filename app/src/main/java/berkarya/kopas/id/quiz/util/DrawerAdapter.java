package berkarya.kopas.id.quiz.util;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import berkarya.kopas.id.quiz.R;


/**
 * Created by darshanz on 7/6/15.
 */
public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.DrawerViewHolder> {

    public final static int TYPE_HEADER = 0;
    public final static int TYPE_MENU = 1;


    private ArrayList<MengerjakanSoal> drawerMenuList;

    private OnItemSelecteListener mListener;

    public DrawerAdapter(ArrayList<MengerjakanSoal> drawerMenuList) {
        this.drawerMenuList = drawerMenuList;
    }

    @Override
    public DrawerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(viewType == TYPE_HEADER){

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_drawer_header, parent, false);

        }else {

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_menu_item, parent, false);
        }

        return new DrawerViewHolder(view, viewType);
    }


    @Override
    public void onBindViewHolder(DrawerViewHolder holder, int position) {
        if(position == 0 ) {
            holder.headerText.setText("Header Text");
        }
        else{

            String nomor = drawerMenuList.get(position - 1).get_nomor();
            String jawaban = drawerMenuList.get(position - 1).get_jawab_soal();
            String ragu = drawerMenuList.get(position - 1).get_jawab_soal_ragu();

            if( ragu.equals("1") ) {
                holder.badge_layout_noterjawab.setVisibility(View.GONE);
                holder.badge_layout_terjawab.setVisibility(View.GONE);
                holder.badge_layout_ragu.setVisibility(View.VISIBLE);
                holder.badge_layout_selected.setVisibility(View.GONE);

                holder.nomor_ragu.setText( nomor );
                holder.jawaban_ragu.setText( jawaban );
                if(jawaban.isEmpty() || jawaban.equals("_")) holder.jawaban_ragu.setVisibility(View.GONE);
            }else if(!jawaban.equals("_")  && (ragu.equals("0") || ragu.isEmpty() || ragu.equals("") )){
                holder.badge_layout_noterjawab.setVisibility(View.GONE);
                holder.badge_layout_terjawab.setVisibility(View.VISIBLE);
                holder.badge_layout_ragu.setVisibility(View.GONE);
                holder.badge_layout_selected.setVisibility(View.GONE);

                holder.nomor_terjawab.setText( nomor );
                holder.jawaban_terjawab.setText( jawaban );
                if(jawaban.isEmpty() || jawaban.equals("_")) holder.jawaban_terjawab.setVisibility(View.GONE);
            }else{
                holder.badge_layout_noterjawab.setVisibility(View.VISIBLE);
                holder.badge_layout_terjawab.setVisibility(View.GONE);
                holder.badge_layout_ragu.setVisibility(View.GONE);
                holder.badge_layout_selected.setVisibility(View.GONE);

                holder.nomor_noterjawab.setText( nomor );
                holder.jawaban_noterjawab.setText( jawaban );
                if(jawaban.isEmpty() || jawaban.equals("_")) holder.jawaban_noterjawab.setVisibility(View.GONE);
            }

        }

    }

    @Override
    public int getItemCount() {

        return drawerMenuList.size()+1 ;

    }



    @Override
    public int getItemViewType(int position) {

        if(position == 0){
            return  TYPE_HEADER;
        }
        else{
            return TYPE_MENU;
        }

    }

    class DrawerViewHolder extends RecyclerView.ViewHolder{

        TextView headerText;
        TextView subtitle;

        LinearLayout linearLayout;
        RelativeLayout badge_layout_noterjawab, badge_layout_terjawab, badge_layout_ragu, badge_layout_selected;
        TextView nomor_noterjawab,nomor_terjawab,nomor_ragu,nomor_selected;
        TextView jawaban_noterjawab,jawaban_terjawab,jawaban_ragu,jawaban_selected;

        TextView textViewTitle;
        View viewDivider;

        public DrawerViewHolder(View itemView, int viewType) {
            super(itemView);


            if(viewType == 0){
                headerText = (TextView)itemView.findViewById(R.id.headerText);
                subtitle=(TextView)itemView.findViewById(R.id.subTitle);

            }
            else {
                linearLayout = itemView.findViewById(R.id.linearlayoutItem);
                badge_layout_noterjawab = itemView.findViewById(R.id.badge_layout_noterjawab);
                badge_layout_terjawab = itemView.findViewById(R.id.badge_layout_terjawab);
                badge_layout_ragu = itemView.findViewById(R.id.badge_layout_ragu);
                badge_layout_selected = itemView.findViewById(R.id.badge_layout_selected);

                nomor_noterjawab = (TextView) itemView.findViewById(R.id.nomor_noterjawab);
                nomor_terjawab = (TextView) itemView.findViewById(R.id.nomor_terjawab);
                nomor_ragu = (TextView) itemView.findViewById(R.id.nomor_ragu);
                nomor_selected = (TextView) itemView.findViewById(R.id.nomor_noterjawab);

                jawaban_noterjawab = (TextView) itemView.findViewById(R.id.jawaban_noterjawab);
                jawaban_terjawab = (TextView) itemView.findViewById(R.id.jawaban_terjawab);
                jawaban_ragu = (TextView) itemView.findViewById(R.id.jawaban_ragu);
                jawaban_selected = (TextView) itemView.findViewById(R.id.jawaban_selected);

            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemSelected(view, getAdapterPosition());

                }
            });
        }

    }




    public void setOnItemClickLister(OnItemSelecteListener mListener) {
        this.mListener = mListener;
    }

   public interface OnItemSelecteListener{
        public void onItemSelected(View v, int position);
    }

}