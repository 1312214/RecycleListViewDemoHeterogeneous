package com.duyhoang.recyclelistviewdemoheterogeneous;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ListAdapterWithRecycleView.PersonModifiedListener,
        ListAdapterWithRecycleView.AdvertiseListener{

    RecyclerView rvList;
    List<Person> personList;
    Spinner spNationalities;
    EditText etFirstName, etLastName;
    RadioButton rbMale, rbFemale;
    Button btnAdd;
    RadioGroup radioGroupGender;

    LinearLayoutManager linearLayoutManager;
    GridLayoutManager gridLayoutManager;
    StaggeredGridLayoutManager staggeredGridLayoutManager;

    Person.GENDER gender;
    String firstname, lastname, nationality;

    int modifiedPosition;
    AppUtil appUtil;
    List<Object> catalogue;

    ListAdapterWithRecycleView listAdapterWithRecycleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();

        appUtil = new AppUtil(this);
//        personList = appUtil.getPersonList();
        catalogue = appUtil.getCatalogue();
//        listAdapterWithRecycleView = new ListAdapterWithRecycleView(this, personList);
        listAdapterWithRecycleView = new ListAdapterWithRecycleView(this, catalogue);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (position % 3 == 0)? 2:1;
            }
        });
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        rvList.setLayoutManager(linearLayoutManager);
        rvList.setAdapter(listAdapterWithRecycleView);
        listAdapterWithRecycleView.setPersonModifiedListener(this);
        listAdapterWithRecycleView.setAdvertiseListener(this);

        btnAdd.setOnClickListener(this);
    }

    private void initUI() {
        rvList = findViewById(R.id.recyclerview_list);
        spNationalities = findViewById(R.id.spinner_nationality);
        etFirstName = findViewById(R.id.edit_text_first_name);
        etLastName = findViewById(R.id.edit_text_last_name);
        rbFemale = findViewById(R.id.radio_button_female);
        rbMale = findViewById(R.id.radio_button_male);
        btnAdd = findViewById(R.id.button_add);
        btnAdd.setTag("add");
        radioGroupGender = findViewById(R.id.radio_group_gender);

    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button_add){
            firstname = etFirstName.getText().toString();
            lastname = etLastName.getText().toString();
            nationality = spNationalities.getSelectedItem().toString();
            Person person = new Person(firstname, lastname, gender, nationality);
            if(isInputValid()){
                String behavior = (String) btnAdd.getTag();
                if(behavior.equalsIgnoreCase("add")){
                    catalogue.add(person);
                    listAdapterWithRecycleView.notifyDataSetChanged();
                    rvList.scrollToPosition(catalogue.size() - 1);
                    clearPersonForm();
                }
                else if(behavior.equalsIgnoreCase("modify")){
                    ((Person)catalogue.get(modifiedPosition)).setName(etFirstName.getText().toString());
                    ((Person)catalogue.get(modifiedPosition)).setLastName(etLastName.getText().toString());
                    if(rbMale.isChecked()){
                        ((Person)catalogue.get(modifiedPosition)).setGender(Person.GENDER.male);
                    }else {
                        ((Person)catalogue.get(modifiedPosition)).setGender(Person.GENDER.female);
                    }

                    ((Person)catalogue.get(modifiedPosition)).setNationality(spNationalities.getSelectedItem().toString());

                    listAdapterWithRecycleView.notifyItemChanged(modifiedPosition);
                    rvList.scrollToPosition(modifiedPosition);
                    clearPersonForm();
                    btnAdd.setText("add");
                    btnAdd.setTag("add");
                }

            }
            else {
                Toast.makeText(this, "Input information is invalid", Toast.LENGTH_SHORT).show();
            }

        }

    }

    private void clearPersonForm() {
        etFirstName.setText("");
        etLastName.setText("");
        radioGroupGender.clearCheck();
        spNationalities.setSelection(0);
    }

    private boolean isInputValid() {
        if(AppUtil.isStringEmpty(firstname) || AppUtil.isStringEmpty(lastname)
                || AppUtil.isStringEmpty(nationality) || gender == null)
            return false;
        return true;
    }


    public void onRadioButtonClicked(View view){
        boolean checked = ((RadioButton)view).isChecked();

        switch (view.getId()){
            case R.id.radio_button_female:
                if(checked){
                    gender = Person.GENDER.female;
                }
                break;

            case R.id.radio_button_male:
                if(checked){
                    gender = Person.GENDER.male;
                }
                break;
        }
    }


    @Override
    public void onPersonDeleted(int position) {

    }

    @Override
    public void onPersonSelected(int position) {
        modifiedPosition = position;
        btnAdd.setTag("modify");
        btnAdd.setText("modify");

        Person currPerson = ((Person)catalogue.get(modifiedPosition));
        etFirstName.setText(currPerson.getName());
        etLastName.setText(currPerson.getLastName());
        if(currPerson.getGender() == Person.GENDER.male){
            rbMale.performClick();
        }
        else
            rbFemale.performClick();
        spNationalities.setSelection(appUtil.getUniqueNationlityBySelectedIndex(currPerson.getNationality()));
    }


    @Override
    public void onAdvertiseClicked(int pos) {
        Toast.makeText(this, "Advertise item: " + pos + "is clicked", Toast.LENGTH_SHORT).show();
    }
}
