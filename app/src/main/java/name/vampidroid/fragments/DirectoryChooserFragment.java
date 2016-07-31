package name.vampidroid.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import name.vampidroid.R;

/**
 * Created by fxjr on 22/07/16.
 */

public class DirectoryChooserFragment extends DialogFragment {

    private static final String TAG = "DirectoryChooserFragmen";

    public static final String CURRENT_DIRECTORY = "CURRENT_DIRECTORY";

    public interface DirectoryChooserFragmentListener {
        void onDirectorySelected(String directoryPath);
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

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(), getTheme());
        alertDialogBuilder.setTitle("Choose images folder");

        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_directory_chooser, null);


        textviewSelectedDirectory = (TextView) dialogView.findViewById(R.id.selectedDirectory);

        if (savedInstanceState != null) {
            selectedDirectory = savedInstanceState.getString(CURRENT_DIRECTORY);
        } else {
            selectedDirectory = getArguments().getString(CURRENT_DIRECTORY);
        }

        textviewSelectedDirectory.setText(selectedDirectory);


        listviewDirectories = (ListView) dialogView.findViewById(R.id.listDirectories);


        listviewDirectories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String itemClicked = (String) adapterView.getAdapter().getItem(i);
                File directory = new File(selectedDirectory);
                if (itemClicked.equals("..")) {
                    selectedDirectory = directory.getParentFile().getAbsolutePath();
                } else {
                    selectedDirectory = selectedDirectory + "/" + itemClicked;
                }

                textviewSelectedDirectory.setText(selectedDirectory);
                showDirectoriesInsideSelectedDirectory();
            }
        });


        directories = new ArrayList<>();

        listDirectoriesAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, directories);

        listviewDirectories.setAdapter(listDirectoriesAdapter);


        alertDialogBuilder.setView(dialogView);
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


        showDirectoriesInsideSelectedDirectory();

        return alertDialogBuilder.create();
    }




    private Runnable fillDirectoriesRunnable = new Runnable() {
        @Override
        public void run() {

            directories.clear();
            directories.add("..");

            File dir = new File(selectedDirectory);


            File[] directoriesList = dir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    // Only accept directories.
                    return file.isDirectory();

                }
            });

            for (File directory : directoriesList) {
                directories.add(directory.getName());
            }


            // In order to update the UI, post it through a widget created in the UI thread.
            listviewDirectories.post(new Runnable() {
                @Override
                public void run() {
                    // Tell list view to refresh its contents.
                    listDirectoriesAdapter.notifyDataSetChanged();

                }
            });
        }

    };


    private void showDirectoriesInsideSelectedDirectory() {

        // Use another thread in order to not block the UI thread if the directory has too many files.
        // As it is the case when selecting the cards image directory as there is a lot of images in it.
        new Thread(fillDirectoriesRunnable).start();


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(CURRENT_DIRECTORY, selectedDirectory);
    }
}
