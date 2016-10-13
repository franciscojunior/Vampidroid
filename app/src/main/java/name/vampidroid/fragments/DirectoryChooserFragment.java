package name.vampidroid.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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

    public static final int GRANT_STORAGE_ACCESS_REQUEST_CODE = 1;

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
                if (itemClicked.equals("..")) {
                    File directory = new File(selectedDirectory);

                    // Check if there is a parent directory. If not, it means we are on root.
                    if (directory.getParentFile() != null) {
                        selectedDirectory = directory.getParentFile().getAbsolutePath();
                    } else {
                        // Continue on root.
                        selectedDirectory = "/";
                    }
                } else {

                    StringBuilder selectedDirectoryBuilder = new StringBuilder();

                    // if we are on root, don't add the heading slash.
                    if (selectedDirectory.equals("/")) {
                        selectedDirectoryBuilder.append("/").append(itemClicked);
                    } else {
                        selectedDirectoryBuilder.append(selectedDirectory).append("/").append(itemClicked);
                    }

                    selectedDirectory = selectedDirectoryBuilder.toString();
                }

                textviewSelectedDirectory.setText(selectedDirectory);
                fillDirectoriesInsideSelectedDirectory();
            }
        });


        directories = new ArrayList<>();

        listDirectoriesAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, directories);

        listviewDirectories.setAdapter(listDirectoriesAdapter);


        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
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


        //    Reference: https://gist.github.com/MariusVolkhart/618a51bb09c4fc7f86a4
        //    Reference: http://stackoverflow.com/questions/33162152/storage-permission-error-in-marshmallow


        // At first, this storage permission was being asked on the SettingsFragment, but I faced a problem
        // when trying to open the DirectoryChooserFragment, the problem is reported on this bug report:
        // https://code.google.com/p/android/issues/detail?id=190966
        // Reference: http://stackoverflow.com/questions/33264031/calling-dialogfragments-show-from-within-onrequestpermissionsresult-causes
        // On the bug report, at this comment: https://code.google.com/p/android/issues/detail?id=190966#c33
        // there is the idea of asking the permission in the fragment itself which is what is done here.

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, GRANT_STORAGE_ACCESS_REQUEST_CODE);
        }


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

            if (directoriesList != null) {
                for (File directory : directoriesList) {
                    directories.add(directory.getName());
                }
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


    private void fillDirectoriesInsideSelectedDirectory() {

        // Use another thread in order to not block the UI thread if the directory has too many files.
        // As it is the case when selecting the cards image directory as there is a lot of images in it.
        new Thread(fillDirectoriesRunnable).start();


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(CURRENT_DIRECTORY, selectedDirectory);
    }


    @Override
    public void onResume() {
        super.onResume();


        // Check permissions again to see if we were granted while the fragment was paused. I.e.: the user went to app settings
        // and granted us the permission.
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            fillDirectoriesInsideSelectedDirectory();
        }

    }
}
