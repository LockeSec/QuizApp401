package com.lockesec.quizapp401;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class DrawerUtil  {

    public static void getDrawer(final Activity activity)
    {
        PrimaryDrawerItem game = new PrimaryDrawerItem().withIdentifier(0).withName("Game").withTextColorRes(R.color.login_form).withIcon(R.drawable.ic_quizapp_logo_24dp);
        PrimaryDrawerItem addQuestion = new PrimaryDrawerItem().withIdentifier(1).withName("Add Question").withTextColorRes(R.color.login_form).withIcon(R.drawable.ic_question_answer_white_24dp);
        PrimaryDrawerItem editProfile = new PrimaryDrawerItem().withIdentifier(2).withName("Edit Profile").withTextColorRes(R.color.login_form).withIcon(R.drawable.ic_profile_circle_white_24dp);
        PrimaryDrawerItem checkLeaderboard = new PrimaryDrawerItem().withIdentifier(3).withName("Check Leaderboard").withTextColorRes(R.color.login_form).withIcon(R.drawable.ic_leaderboard_white_24dp);
        PrimaryDrawerItem signOut = new PrimaryDrawerItem().withIdentifier(4).withName("Sign Out").withTextColorRes(R.color.login_form).withIcon(R.drawable.ic_logout_white_24dp);

        AccountHeader header = createProfileHeader(activity);

        Drawer result = new DrawerBuilder()
                .withActivity(activity)
                .withAccountHeader(header)
                .withTranslucentStatusBar(true)
                .withActionBarDrawerToggle(false)
                .withActionBarDrawerToggleAnimated(true)
                .withSliderBackgroundDrawableRes(R.drawable.bg)
                .withCloseOnClick(true)
                .withSelectedItem(-1)
                .addDrawerItems(
                        game,
                        addQuestion,
                        editProfile,
                        checkLeaderboard,
                        signOut
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch (position)
                        {
                            case 1:
                                if(activity.getClass() != MainActivity.class)
                                    view.getContext().startActivity(new Intent(activity, MainActivity.class));
                                break;
                            case 2:
                                if(activity.getClass() != AddQuestionActivity.class)
                                    view.getContext().startActivity(new Intent(activity, AddQuestionActivity.class));
                                break;

                            case 3:
                                if(activity.getClass() != ProfileActivity.class)
                                    view.getContext().startActivity(new Intent(activity, ProfileActivity.class));
                                break;

                            case 4:
                                if(activity.getClass() != LeaderboardActivity.class)
                                    view.getContext().startActivity(new Intent(activity, LeaderboardActivity.class));
                                break;

                            case 5:
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(activity, SignUpActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                view.getContext().startActivity(intent);
                                break;
                        }
                        return true;
                    }
                })
                .build();
    }



    public static AccountHeader createProfileHeader(Activity activity)
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String name = user.getDisplayName();
        String email = user.getEmail();
        String profilePicURL = user.getPhotoUrl().toString();

        downloadFile(profilePicURL, "/res/drawable/");

        ProfileDrawerItem profile = new ProfileDrawerItem();
        profile.withName(name).withEmail(email).withIcon(R.drawable.camera);

        AccountHeader header = new AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(R.color.material_drawer_dark_background)
                .addProfiles(profile)
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        return false;
                    }
                })
                .build();

        return header;
    }

    private static void downloadFile(String url, File outputFile) {
        try {
            URL u = new URL(url);
            URLConnection conn = u.openConnection();
            int contentLength = conn.getContentLength();

            DataInputStream stream = new DataInputStream(u.openStream());

            byte[] buffer = new byte[contentLength];
            stream.readFully(buffer);
            stream.close();

            DataOutputStream fos = new DataOutputStream(new FileOutputStream(outputFile));
            fos.write(buffer);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            return;
        } catch (IOException e) {
            return;
        }
    }
}
