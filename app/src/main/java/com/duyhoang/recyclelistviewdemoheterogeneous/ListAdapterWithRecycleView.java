package com.duyhoang.recyclelistviewdemoheterogeneous;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import java.util.List;

/**
 * Created by rogerh on 7/26/2018.
 */

public class ListAdapterWithRecycleView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static String TAG = ListAdapterWithRecycleView.class.getSimpleName();

//    private List<Person> personList;
    private Context context;
    private PersonModifiedListener personModifiedListener;
    private AdvertiseListener advertiseListener;
    private List<Object> catalogue;



//    public ListAdapterWithRecycleView(Context context, List<Person> listPerson){
//        this.context = context;
//        this.personList = listPerson;
//    }

    public ListAdapterWithRecycleView(Context context, List<Object> catalogue){
        this.context = context;
        this.catalogue = catalogue;
    }

    public interface PersonModifiedListener {
        void onPersonDeleted(int position);
        void onPersonSelected(int position);
    }


    public void setPersonModifiedListener(PersonModifiedListener personModifiedListener){
        this.personModifiedListener = personModifiedListener;
    }

    public interface AdvertiseListener {
        void onAdvertiseClicked(int pos);
    }

    public void setAdvertiseListener(AdvertiseListener advertiseListener){
        this.advertiseListener = advertiseListener;
    }


    class PersonViewHolder extends RecyclerView.ViewHolder{
        TextView name, lastname, gender, nationality;


        public PersonViewHolder(View view){
            super(view);
            name = view.findViewById(R.id.text_name);
            lastname = view.findViewById(R.id.text_lastname);
            gender = view.findViewById(R.id.text_gender);
            nationality = view.findViewById(R.id.text_nationality);
        }
    }


    class AdvertiseViewHolder extends RecyclerView.ViewHolder{
        TextView textAdvertise;

        public AdvertiseViewHolder(View view){
            super(view);
            textAdvertise = view.findViewById(R.id.text_advertise_text);
        }
    }

//    @Override
//    public int getItemViewType(int position) {
//        if(position % 3 == 0)
//            return R.layout.layout_ad_row_item;
//        else
//            return R.layout.layout_person_row_item;
//    }

    @Override
    public int getItemViewType(int position) {
        if(catalogue.get(position) instanceof  Advertisement)
            return R.layout.layout_ad_row_item;
        else if(catalogue.get(position) instanceof Person)
            return R.layout.layout_person_row_item;
        return -1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final RecyclerView.ViewHolder holder;
        View view;
        switch (viewType){
            case R.layout.layout_person_row_item:
                view = LayoutInflater.from(context).inflate(R.layout.layout_person_row_item, parent, false);
                holder = new PersonViewHolder(view);

                view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        int pos = holder.getAdapterPosition();
                        catalogue.remove(pos);
                        Toast.makeText(context, "Item at " + pos + " deleted !", Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                        if(personModifiedListener != null){
                            personModifiedListener.onPersonDeleted(pos);}
                        return true;
                    }
                });

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = holder.getAdapterPosition();
                        if(personModifiedListener != null){
                            personModifiedListener.onPersonSelected(pos);}
                    }
                });

                break;

            case R.layout.layout_ad_row_item:
                view = LayoutInflater.from(context).inflate(R.layout.layout_ad_row_item, parent, false);
                holder = new AdvertiseViewHolder(view);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = holder.getAdapterPosition();
                        if(advertiseListener != null)
                            advertiseListener.onAdvertiseClicked(pos);
                    }
                });

                break;
                default:
                    view = LayoutInflater.from(context).inflate(R.layout.layout_ad_row_item, parent, false);
                    holder = new AdvertiseViewHolder(view);
        }

        Log.e(TAG, "onCreateViewHolder - invoked: ");
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof PersonViewHolder){
            final Person person = (Person) catalogue.get(position);
            final PersonViewHolder personViewHolder = (PersonViewHolder)holder;
            personViewHolder.name.setText(person.getName());
            personViewHolder.lastname.setText(person.getLastName());
            personViewHolder.gender.setText((person.getGender() == Person.GENDER.male) ? "male": "female");
            personViewHolder.nationality.setText(person.getNationality());

            personViewHolder.gender.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = personViewHolder.getAdapterPosition();
                    Person.GENDER gender = ((Person)catalogue.get(pos)).getGender();
                    if(gender == Person.GENDER.male){
                        ((Person)catalogue.get(pos)).setGender(Person.GENDER.female);
                    }
                    else {
                        ((Person)catalogue.get(pos)).setGender(Person.GENDER.male);
                    }
                    notifyItemChanged(pos);

                }
            });
        }
        else if(holder instanceof  AdvertiseViewHolder){
            String msg = ((Advertisement)catalogue.get(position)).getMessage();
            ((AdvertiseViewHolder)holder).textAdvertise.setText(msg) ;
            ((AdvertiseViewHolder)holder).textAdvertise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Advertise text is not available", Toast.LENGTH_SHORT).show();
                }
            });
        }

        Log.e(TAG, "onBindViewHolder - invoked: " + position);
    }

    @Override
    public int getItemCount() {
        return catalogue.size();
    }
}
