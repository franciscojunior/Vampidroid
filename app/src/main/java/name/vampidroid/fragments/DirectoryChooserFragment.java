package name.vampidroid.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import name.vampidroid.R;

/**
 * Created by fxjr on 22/07/16.
 */

public class DirectoryChooserFragment extends AppCompatDialogFragment {

    private static final String TAG = "DirectoryChooserFragmen";

    public static final String CURRENT_DIRECTORY = "CURRENT_DIRECTORY";


    public interface DirectoryChooserFragmentListener {

        public void onDirectorySelected(String directoryPath);
    }


    private TextView textviewSelectedDirectory;
    private ListView listviewDirectories;
    private ArrayAdapter<String> listDirectoriesAdapter;
    private List<String> directories;
    private String selectedDirectory;


    public static DirectoryChooserFragment newInstance(String initialDirectoryName) {

        Bundle args = new Bundle();

        args.putString(CURRENT_DIRECTORY, initialDirectoryName);

        DirectoryChooserFragment fragment = new DirectoryChooserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        String title = getArguments().getString("title");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Choose images folder");

        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_directory_chooser, null);


        textviewSelectedDirectory = (TextView) v.findViewById(R.id.selectedDirectory);
        selectedDirectory = getArguments().getString(CURRENT_DIRECTORY);
        textviewSelectedDirectory.setText(selectedDirectory);


        listviewDirectories = (ListView) v.findViewById(R.id.listDirectories);


        listviewDirectories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "onItemClick: " + adapterView.getAdapter().getItem(i));

                String itemClicked = (String) adapterView.getAdapter().getItem(i);
                File directory = new File(selectedDirectory);
                if (itemClicked.equals("..")) {
                    selectedDirectory = directory.getParentFile().getAbsolutePath();
                } else {
                    selectedDirectory = selectedDirectory + "/" + itemClicked;
                }

                textviewSelectedDirectory.setText(selectedDirectory);
                showDirectoriesInPath(selectedDirectory);
            }
        });


        directories = new ArrayList<>();

        listDirectoriesAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, directories);

        listviewDirectories.setAdapter(listDirectoriesAdapter);

        showDirectoriesInPath(selectedDirectory);

        alertDialogBuilder.setView(v);
        alertDialogBuilder.setPositiveButton("OK",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Fragment parent = getTargetFragment();

                if (parent instanceof DirectoryChooserFragmentListener) {
                    DirectoryChooserFragmentListener listener = (DirectoryChooserFragmentListener) parent;
                    listener.onDirectorySelected(selectedDirectory);

                }

            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        return alertDialogBuilder.create();
    }

    private void showDirectoriesInPath(String selectedDirectory) {

        Log.d(TAG, "showDirectoriesInPath() called with: textviewSelectedDirectory = [" + selectedDirectory + "]");

        directories.clear();
        directories.add("..");

        File dir = new File(selectedDirectory);

        File[] directoriesList = dir.listFiles();

        for (File directory : directoriesList) {
            if (directory.isDirectory()) {
                directories.add(directory.getName());
            }
        }

        // Tell list view to refresh its contents.
        listDirectoriesAdapter.notifyDataSetChanged();

    }
}
