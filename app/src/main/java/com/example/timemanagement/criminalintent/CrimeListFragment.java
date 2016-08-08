package com.example.timemanagement.criminalintent;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TimeManagement on 3/29/2015.
 */
public class CrimeListFragment extends ListFragment
{
    private ArrayList<Crime>mCrimes;//ArrayList of crimes
    private static final String TAG="CrimeListFragment";
    private boolean mSubtitleVisible;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        mSubtitleVisible=false;
        /*
        getActivity() is a Fragment convenience method that returns the hosting activity and allows a fragment to handle more of the activity's affairs.
        Here you use it to call Activity.setTitle(int) to change what is displayed on the action bar.
        NOTE: You dont need to override onCreateView or inflate any layouts because ListFragment has a defualt view for lists.
         */
        getActivity().setTitle(R.string.crimes_title);//This method returns the hosting activity and allows a fragment to handle more of the activity's affairs
        mCrimes=CrimeLab.get(getActivity()).getCrimes();

      CrimeAdapter adapter=new CrimeAdapter(mCrimes);
        /*
        The setListAdapter(ListAdapter) method is a ListFragment convenience method that you can use to set the adapter of the implicit ListView managed
        by CrimeListFragment. The layout that you specify in teh adapter's constructor is a pre-defined layout from resources provided by the android SDK. This
        layout has TextView as its root element.
         */
        setListAdapter(adapter);
    }

    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup parent,Bundle savedInstanceState)
    {
        View v=inflater.inflate(R.layout.empty_list,parent,false);
        ListView listview=(ListView)v.findViewById(android.R.id.list);
        listview.setEmptyView(listview.findViewById(android.R.id.empty));





        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            if(mSubtitleVisible)
            {
                ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle(R.string.subtitle);
            }
        }
        else
        {
            listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            listview.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    MenuInflater inflater=mode.getMenuInflater();
                    inflater.inflate(R.menu.crime_list_item_context,menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    switch (item.getItemId())
                    {
                        case R.id.menu_item_delete_crime:
                            CrimeAdapter adapter=(CrimeAdapter)getListAdapter();
                            CrimeLab crimeLab=CrimeLab.get(getActivity());
                            for(int i=adapter.getCount()-1;i>=0; i--)
                            {
                                if(getListView().isItemChecked(i))
                                {
                                    crimeLab.deleteCrime(adapter.getItem(i));
                                }
                            }
                            mode.finish();
                            adapter.notifyDataSetChanged();
                            return true;
                        default:
                            return false;
                    }

                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {

                }
            });
        }
        //ListView listView=(ListView)v.findViewById(android.R.id.list);
        registerForContextMenu(listview);
     //   View view=v.findViewById(R.id.empty_view);


        return v;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);
        MenuItem showSubtitle=menu.findItem(R.id.menu_item_show_subtitle);
        if(mSubtitleVisible && showSubtitle !=null)
            showSubtitle.setTitle(R.string.hide_subtitle);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu,View v,ContextMenu.ContextMenuInfo menuInfo)
    {
    getActivity().getMenuInflater().inflate(R.menu.crime_list_item_context, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.menu_item_new_crime:
                Crime crime=new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent i=new Intent(getActivity(),CrimePagerActivity.class);
                i.putExtra(CrimeFragment.EXTRA_CRIME_ID,crime.getmId());
                startActivityForResult(i,0);
                return true;
            case R.id.menu_item_show_subtitle:
                if(((AppCompatActivity)getActivity()).getSupportActionBar().getSubtitle()==null) {
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(R.string.subtitle);
                    mSubtitleVisible=true;
                    item.setTitle(R.string.hide_subtitle);
                }
                else
                {
                    ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle(R.string.show_subtitle);
                    mSubtitleVisible=false;
                    item.setTitle(R.string.show_subtitle);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();


    }
    @Override
    public void onResume()
    {
        super.onResume();
   //     View v=getActivity().getLayoutInflater().inflate(R.layout.empty_list,null);
   //     getListView().setEmptyView(v.findViewById(android.R.id.empty));
        ((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onListItemClick(ListView l,View v,int position,long id)
    {
        //since you are casting to CrimeAdapter,you get the benefits of type-checking. CrimeAdapter can only hold Crime objects,so you no longer need to
        //cast to crime.
        Crime c=((CrimeAdapter)getListAdapter()).getItem(position);

        //start CrimeActivity
        /*
        Here CrimeListFragment creates an explicit intent that names the CrimeActivity class. CrimeListFragment uses the getActivity() method to pass
        its hosting activity as the Context object that the Intent constructor requires.
         */
        Intent i=new Intent(getActivity(),CrimePagerActivity.class);
        i.putExtra(CrimeFragment.EXTRA_CRIME_ID, c.getmId());
        startActivity(i);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int position=info.position;
        CrimeAdapter adapter=(CrimeAdapter)getListAdapter();
        Crime crime=adapter.getItem(position);

        switch(item.getItemId())
        {
            case R.id.menu_item_delete_crime:
                CrimeLab.get(getActivity()).deleteCrime(crime);
                adapter.notifyDataSetChanged();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private class CrimeAdapter extends ArrayAdapter<Crime>
    {
        public CrimeAdapter(ArrayList<Crime>crimes)
        {
            /*
            The call to the superclass contructor is required to properly hook up your dataset of Crimes. You will not be using a pre-defined layout
            so you can pass 0 for the layout ID.
             */
            super(getActivity(),0,crimes);
        }


        @Override
        public View getView(int position,View convertView,ViewGroup parent)
        {
            //if we weren't given a view,inflate one
            if(convertView==null)
            {
                convertView=getActivity().getLayoutInflater().inflate(R.layout.list_item_crime,null);
            }

            //configure the view for this crime
            //get crime object based on position
            Crime c=getItem(position);

                TextView titleTextView = (TextView) convertView.findViewById(R.id.crime_list_item_titleTextView);
                titleTextView.setText(c.getmTitle());

                TextView dateTextView = (TextView) convertView.findViewById(R.id.crime_list_item_dateTextView);
               // dateTextView.setText(c.getmDate().toString());

                CheckBox solvedCheckBox = (CheckBox) convertView.findViewById(R.id.crime_list_item_solvedCheckBox);
                solvedCheckBox.setChecked(c.isSolved());


            return convertView;
        }
    }

}
