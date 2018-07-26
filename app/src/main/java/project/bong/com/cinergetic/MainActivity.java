package project.bong.com.cinergetic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import project.bong.com.cinergetic.fragment.AdminCheckFragment;
import project.bong.com.cinergetic.fragment.AdminPostFragment;
import project.bong.com.cinergetic.fragment.AdminRegistFragment;
import project.bong.com.cinergetic.fragment.DetailFragment;
import project.bong.com.cinergetic.fragment.FootprintsFragment;
import project.bong.com.cinergetic.fragment.MainFragment;
import project.bong.com.cinergetic.fragment.MapFragment;
import project.bong.com.cinergetic.fragment.StoryFragment;
import project.bong.com.cinergetic.fragment.SupportFragment;
import project.bong.com.cinergetic.model.LoginToken;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static LoginToken userToken;

    // activity number
    public static final int ACTIVITY_LOGIN = 101;
    public static final int ACTIVITY_SIGNUP = 102;
    
    // fragment number
    public final int FRAGMENT_MAIN = 1000;
    public final int FRAGMENT_STORY = 1001;
    public final int FRAGMENT_MAP = 1002;
    public final int FRAGMENT_FOOTPRINTS = 1003;
    public final int FRAGMENT_SUPPORT = 1004;
    public final int FRAGMENT_ADMIN_REGIST = 1005;
    public final int FRAGMENT_ADMIN_REGIST_DIAL = 1006;
    public final int FRAGMENT_ADMIN_POST = 1007;
    public final int FRAGMENT_ADMIN_CHECK = 1008;
    public final int FRAGMENT_DETAIL = 1009;

    // fragment
    private MainFragment mainFragment;
    private StoryFragment storyFragment;
    private MapFragment mapFragment;
    private FootprintsFragment footprintsFragment;
    private SupportFragment supportFragment;
    private AdminRegistFragment adminRegistFragment;
    private AdminPostFragment adminPostFragment;
    private AdminCheckFragment adminCheckFragment;
    private DetailFragment detailFragment;
    FragmentManager manager;

    DrawerLayout drawer;
    NavigationView navigationView;
    View headerview;
    TextView login, signup, logout;
    ImageView btn_menu_story;
    ImageView btn_menu_support;
    ImageView btn_menu_main;
    ImageView btn_menu_map;
    ImageView btn_menu_mypage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 프래그먼트(mainFragment) 설정
        mainFragment = new MainFragment();
        storyFragment = new StoryFragment();
        mapFragment = new MapFragment();
        footprintsFragment = new FootprintsFragment();
        adminRegistFragment = new AdminRegistFragment();
        supportFragment= new SupportFragment();
        adminPostFragment = new AdminPostFragment();
        adminCheckFragment = new AdminCheckFragment();
        detailFragment = new DetailFragment();

        manager = getSupportFragmentManager();
        manager.beginTransaction().add(R.id.fragment_box, mainFragment).commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);      // 툴바 그림자 제거



        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // 네비게이션 헤더에 있는 뷰 가져오기
        headerview = navigationView.getHeaderView(0);
        login = (TextView)headerview.findViewById(R.id.login);
        signup = (TextView)headerview.findViewById(R.id.signup);
        logout = (TextView)headerview.findViewById(R.id.logout);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_login = new Intent(getApplicationContext(), LoginActivity.class);
                startActivityForResult(intent_login, ACTIVITY_LOGIN);
            }

        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_singup = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent_singup, ACTIVITY_SIGNUP);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 관리자모드였다면 메뉴 원래대로 돌려주기
                if(userToken.getAdmin()){
                    navigationView.getMenu().clear();
                    navigationView.inflateMenu(R.menu.activity_user_drawer);
                }
                // 로그인 토큰 삭제
                userToken = null;
                login.setVisibility(View.VISIBLE);
                signup.setVisibility(View.VISIBLE);
                logout.setVisibility(View.INVISIBLE);

                // 메인 프레그먼트로 이동 & drawer 닫아주기
                changeFragment(FRAGMENT_MAIN);
                drawer.closeDrawer(GravityCompat.START);
                Toast.makeText(getApplicationContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // 하단 메뉴 버튼 (클릭시 각각 프래그먼트로 이동)
        btn_menu_story = (ImageView)findViewById(R.id.menu_btn_story);
        btn_menu_support = (ImageView)findViewById(R.id.menu_btn_support);
        btn_menu_main = (ImageView)findViewById(R.id.menu_btn_main);
        btn_menu_map = (ImageView)findViewById(R.id.menu_btn_map);
        btn_menu_mypage = (ImageView)findViewById(R.id.menu_btn_mypage);

        btn_menu_story.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { changeFragment(FRAGMENT_STORY);
            }
        });
        btn_menu_support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { changeFragment(FRAGMENT_SUPPORT);
            }
        });
        btn_menu_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(FRAGMENT_MAIN);
            }
        });
        btn_menu_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(FRAGMENT_MAP);
            }
        });
        btn_menu_mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userToken == null){
                    Intent intent_login = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivityForResult(intent_login, ACTIVITY_LOGIN);
                }else{
                    Toast.makeText(getApplicationContext(), "로그인됐을때는 어디로 이동하남", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    @Override  // 뒤로가기 버튼 누를 때 실행되는 함수
    public void onBackPressed() {
        // Naviagtion darawer가 나와있을 때 뒤로가기를 누르면 네비가 들어가고,
        // 안나와 있으면 앱이 종료된다.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override   // 오른쪽 상단
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // 네비게이션 아이템(목록) 클릭 시 실행되는 메소드
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case R.id.nav_story:
                changeFragment(FRAGMENT_STORY);
                break;
            case R.id.nav_map:
                changeFragment(FRAGMENT_MAP);
                break;
            case R.id.nav_footprints:
                changeFragment(FRAGMENT_FOOTPRINTS);
                break;
            case R.id.nav_support:
                Toast.makeText(this, "후원페이지입니다.", Toast.LENGTH_SHORT).show();
                //changeFragment(SUPPORT_FRAGMENT);
                break;
            case R.id.nav_regist:
                changeFragment(FRAGMENT_ADMIN_REGIST);
                break;
            case R.id.nav_post:
                changeFragment(FRAGMENT_ADMIN_POST);
                break;
            case R.id.nav_check:
                changeFragment(FRAGMENT_ADMIN_CHECK);
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void changeFragment(int index){
        switch(index){
            case FRAGMENT_MAIN:
                manager.beginTransaction().replace(R.id.fragment_box, mainFragment).commit();
                break;
            case FRAGMENT_STORY:
                manager.beginTransaction().replace(R.id.fragment_box, storyFragment).commit();
                break;
            case FRAGMENT_MAP:
                manager.beginTransaction().replace(R.id.fragment_box, mapFragment).commit();
                break;
            case FRAGMENT_FOOTPRINTS:
                manager.beginTransaction().replace(R.id.fragment_box, footprintsFragment).commit();
                break;
            case FRAGMENT_SUPPORT:
                manager.beginTransaction().replace(R.id.fragment_box, supportFragment).commit();
                break;
            case FRAGMENT_ADMIN_REGIST:
                manager.beginTransaction().replace(R.id.fragment_box, adminRegistFragment).commit();
                break;
            case FRAGMENT_ADMIN_POST:
                manager.beginTransaction().replace(R.id.fragment_box, adminPostFragment).commit();
                break;
            case FRAGMENT_ADMIN_CHECK:
                manager.beginTransaction().replace(R.id.fragment_box, adminCheckFragment).commit();
                break;
            case FRAGMENT_DETAIL:
                manager.beginTransaction().replace(R.id.fragment_box, detailFragment).commit();
                break;
        }
        changeBottomMenuColor(index);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case ACTIVITY_LOGIN:
                if(userToken != null){
                    // 메뉴아이템 로그인쪽 & 관리자메뉴 변경해줘야 함!
                    if(userToken.getAdmin()) {
                        Toast.makeText(getApplicationContext(), "관리자모드로 로그인되었습니다.", Toast.LENGTH_LONG).show();
                        navigationView.getMenu().clear();
                        navigationView.inflateMenu(R.menu.activity_admin_drawer);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), userToken.getName() + "님 환영합니다.", Toast.LENGTH_LONG).show();
                    }

                    login.setVisibility(View.INVISIBLE);
                    signup.setVisibility(View.INVISIBLE);
                    logout.setVisibility(View.VISIBLE);

                }else{
                    Toast.makeText(getApplicationContext(), "Login token is null", Toast.LENGTH_LONG).show();
                }
                break;
            case ACTIVITY_SIGNUP:
                break;
            default:
                Toast.makeText(getApplicationContext(), "default", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void changeBottomMenuColor(int fragment_id){
        switch (fragment_id){
            case FRAGMENT_STORY:
                btn_menu_story.setImageResource(R.drawable.storyiconover);
                btn_menu_map.setImageResource(R.drawable.mapicon);
                btn_menu_main.setImageResource(R.drawable.mainicon);
                btn_menu_support.setImageResource(R.drawable.supporticon);
                btn_menu_mypage.setImageResource(R.drawable.mypageicon);
                break;
            case FRAGMENT_MAP:
                btn_menu_story.setImageResource(R.drawable.storyicon);
                btn_menu_map.setImageResource(R.drawable.mapiconover);
                btn_menu_main.setImageResource(R.drawable.mainicon);
                btn_menu_support.setImageResource(R.drawable.supporticon);
                btn_menu_mypage.setImageResource(R.drawable.mypageicon);
                break;
            case FRAGMENT_MAIN:
                btn_menu_story.setImageResource(R.drawable.storyicon);
                btn_menu_map.setImageResource(R.drawable.mapicon);
                btn_menu_main.setImageResource(R.drawable.mainiconover);
                btn_menu_support.setImageResource(R.drawable.supporticon);
                btn_menu_mypage.setImageResource(R.drawable.mypageicon);
                break;
            case FRAGMENT_SUPPORT:
                btn_menu_story.setImageResource(R.drawable.storyicon);
                btn_menu_map.setImageResource(R.drawable.mapicon);
                btn_menu_main.setImageResource(R.drawable.mainicon);
                btn_menu_support.setImageResource(R.drawable.supporticonover);
                btn_menu_mypage.setImageResource(R.drawable.mypageicon);
                break;
            /*case FRAGMENT_MYPAGE:
                btn_menu_story.setImageResource(R.drawable.storyicon);
                btn_menu_map.setImageResource(R.drawable.mapicon);
                btn_menu_main.setImageResource(R.drawable.mainicon);
                btn_menu_support.setImageResource(R.drawable.supporticon);
                btn_menu_mypage.setImageResource(R.drawable.mypageiconover);
                break;*/
        }
    }

    public AdminRegistFragment getAdminRegistFragment(){
        return adminRegistFragment;
    }

    public MainFragment getMainFragment() {
        return mainFragment;
    }

    public DetailFragment getDetailFragment() {
        return detailFragment;
    }
}
