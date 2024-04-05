package com.project.controller;
import com.project.entity.Preferences;
import com.project.entity.Profile;
import com.project.entity.User;
import com.project.pojo.*;
import com.project.repo.ProfileRepository;
import com.project.repo.UserRepository;
import com.project.repo.PreferencesRepository;
import com.project.session.SessionSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class AppController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProfileRepository profileRepository;

// dependency injection : run time dependency injection
    @Autowired
    PreferencesRepository prefRepo;

    @RequestMapping(value = "/index", method = GET)
    public String index() {


        return "index";
    }

//Local host:8080 --> Login Page
// session --> Singleton Class
    @RequestMapping(value = "/", method = GET)
    public String loginStart(Model model) {

        if(SessionSingleton.getInstance().getUsername()!=null &&
                !SessionSingleton.getInstance().getUsername().isEmpty()){
            model.addAttribute("username", SessionSingleton.getInstance().getUsername());
            return "homemenu";
        }


        return "login";
    }

    @RequestMapping(value = "/gamestartbed", method = GET)
    @ResponseBody
    public String gameStartBed(Model model) {

        if(SessionSingleton.getInstance().getUsername()!=null &&
                !SessionSingleton.getInstance().getUsername().isEmpty()){
            model.addAttribute("username", SessionSingleton.getInstance().getUsername());
            return "Hello " + SessionSingleton.getInstance().getActiveProfileName();
        }

        return "Hello";
    }
    @RequestMapping(value = "/gamecompletebed", method = GET)
    @ResponseBody
    public String gameCompletedBed(Model model) {

        if(SessionSingleton.getInstance().getUsername()!=null &&
                !SessionSingleton.getInstance().getUsername().isEmpty()){
            model.addAttribute("username", SessionSingleton.getInstance().getUsername());


            Profile profile = profileRepository.findByProfileName(SessionSingleton.getInstance().getActiveProfileName());
            profile.setPlayedBedroom(profile.getPlayedBedroom()+1);
            profileRepository.save(profile);


            List<Preferences> pref = prefRepo.findByUserNameAndProfileNameAndPrefName(SessionSingleton.getInstance().getUsername(),
                    SessionSingleton.getInstance().getActiveProfileName(),
                    SessionSingleton.getInstance().getActivePreference());

            return pref.get(0).getMessageBedroom();
        }

        return "Great Job!!";
    }

    @RequestMapping(value = "/gamestartkitchen", method = GET)
    @ResponseBody
    public String gameStartKitchen(Model model) {

        if(SessionSingleton.getInstance().getUsername()!=null &&
                !SessionSingleton.getInstance().getUsername().isEmpty()){
            model.addAttribute("username", SessionSingleton.getInstance().getUsername());
            return "Hello " + SessionSingleton.getInstance().getActiveProfileName();
        }

        return "Hello";
    }
    @RequestMapping(value = "/gamecompletekitchen", method = GET)
    @ResponseBody
    public String gameCompletedKitchen(Model model) {

        if(SessionSingleton.getInstance().getUsername()!=null &&
                !SessionSingleton.getInstance().getUsername().isEmpty()){
            model.addAttribute("username", SessionSingleton.getInstance().getUsername());


            Profile profile = profileRepository.findByProfileName(SessionSingleton.getInstance().getActiveProfileName());
            profile.setPlayedKitchen(profile.getPlayedKitchen()+1);
            profileRepository.save(profile);

            List<Preferences> pref = prefRepo.findByUserNameAndProfileNameAndPrefName(SessionSingleton.getInstance().getUsername(),
                    SessionSingleton.getInstance().getActiveProfileName(),
                    SessionSingleton.getInstance().getActivePreference());



            return pref.get(0).getMessageKitchen();
        }

        return "Great Job!!";
    }

// logout : session value NULL
    @RequestMapping(value = "/logout", method = GET)
    public String logout(Model model) {


        SessionSingleton.getInstance().setUsername(null);
        SessionSingleton.getInstance().setActiveProfileName(null);
        SessionSingleton.getInstance().setActivePreference(null);


        return "/login";
    }

    @PostMapping("/login")
    // Model Map is used to pass backend data to UI AND @ModelAttribute is used to receive data from UI to backedn through pojo classes

    public String loginEnd(ModelMap model, @ModelAttribute Login login) {


        User user = userRepository.findByUsername(login.getUsername());

        if(user !=null) {
            String encodedPassword = user.getPassword();
            byte[] decodedBytes = Base64.getDecoder().decode(encodedPassword);
            String decodedString = new String(decodedBytes);

            if (decodedString.equals(login.getPassword())) {
                SessionSingleton.getInstance().setUsername(login.getUsername());
                model.addAttribute("username", login.getUsername());
                return "homemenu";
            }
        }
        model.addAttribute("error","Invalid Username/Password");
        return "login";
    }

    @GetMapping("/signup")
    public String signUpStart() {
        return "register";
    }

    @PostMapping("/signup")
    public  String registerUser(Model model,@ModelAttribute SignUp signUp){
        User user = new User();
        user.setUsername(signUp.getUsername());
        String encodedString = Base64.getEncoder().encodeToString( signUp.getPassword().getBytes());
        user.setPassword(encodedString);
        user.setEmail(signUp.getEmail());
//Save user information
        userRepository.save(user);
//set username in singleton session
        SessionSingleton.getInstance().setUsername(signUp.getUsername());
        model.addAttribute("username", SessionSingleton.getInstance().getUsername());

        return "homemenu";
    }


    @GetMapping("/custom")
    public String custom(Model model) {

        return "custom";
    }
// Game start
    @PostMapping("/start")
    public String startGame(@RequestParam String preferenceName, Model model) {

        SessionSingleton.getInstance().setActivePreference(preferenceName);


        String username = SessionSingleton.getInstance().getUsername();

        String profileName = SessionSingleton.getInstance().getActiveProfileName();

        Preferences pref =prefRepo.findByUserNameAndProfileNameAndPrefName(username,profileName,preferenceName).get(0);

        //Theme
        new File("src/main/resources/static/images/tiledbackground2.png").delete();
        moveImage(pref.getTheme(), "tiledbackground2.png");

        //Cupboard
        new File("src/main/resources/static/images/npc22-sheet0.png").delete();
        moveImage( pref.getCupboard(), "npc22-sheet0.png");

        //Chair
        new File("src/main/resources/static/images/npc14-sheet0.png").delete();
        moveImage( pref.getChair(), "npc14-sheet0.png");
        new File("src/main/resources/static/images/npc15-sheet0.png").delete();
        moveImage( pref.getChair(), "npc15-sheet0.png");
        new File("src/main/resources/static/images/npc16-sheet0.png").delete();
        moveImage( pref.getChair(), "npc16-sheet0.png");

        //Microwave
        new File("src/main/resources/static/images/npc18-sheet0.png").delete();
        moveImage( pref.getMicrowave(), "npc18-sheet0.png");

        //Mirror
        new File("src/main/resources/static/images/npc9-sheet0.png").delete();
        moveImage( pref.getMirror(), "npc9-sheet0.png");

        //Wardrobe
        new File("src/main/resources/static/images/npc8-sheet0.png").delete();
        moveImage( pref.getWardrobe(), "npc8-sheet0.png");

        //Recliner
        new File("src/main/resources/static/images/npc7-sheet0.png").delete();
        moveImage( pref.getRecliner(), "npc7-sheet0.png");

        //Fridge
        new File("src/main/resources/static/images/npc12-sheet0.png").delete();
        moveImage( pref.getFridge(), "npc12-sheet0.png");
        new File("src/main/resources/static/images/npc12-sheet1.png").delete();
        moveImage( pref.getFridge(), "npc12-sheet1.png");

        //Couch
        new File("src/main/resources/static/images/npc6-sheet0.png").delete();
        moveImage( pref.getCouch(), "npc6-sheet0.png");

        //Lamp
        new File("src/main/resources/static/images/npc11-sheet0.png").delete();
        moveImage( pref.getLamp(), "npc11-sheet0.png");
        new File("src/main/resources/static/images/npc11-sheet1.png").delete();
        moveImage( pref.getLamp(), "npc11-sheet1.png");

        //Computer
        new File("src/main/resources/static/images/npc3-sheet0.png").delete();
        moveImage( pref.getComputer(), "npc3-sheet0.png");
        new File("src/main/resources/static/images/npc3-sheet1.png").delete();
        moveImage( pref.getComputer(), "npc3-sheet1.png");
        new File("src/main/resources/static/images/npc3-sheet2.png").delete();
        moveImage( pref.getComputer(), "npc3-sheet2.png");

        //Bed
       new File("src/main/resources/static/images/npc5-sheet0.png").delete();
        moveImage( pref.getBed(), "npc5-sheet0.png");

        //TV
        new File("src/main/resources/static/images/npc2-sheet0.png").delete();
        moveImage( pref.getTv(), "npc2-sheet0.png");

        List<Preferences> preferencesList = prefRepo.findByUserNameAndProfileName(SessionSingleton.getInstance().getUsername(), profileName);


        model.addAttribute("profileName", SessionSingleton.getInstance().getActiveProfileName());
        model.addAttribute("preferencesList",  preferencesList);
        model.addAttribute("gameLink", "http://localhost:8080/index");
        model.addAttribute("gameMessage",
                "The game has been generated according to the selected preference - Please give the below link to " + SessionSingleton.getInstance().getActiveProfileName() + " for them to access the game");
        return "profile";
    }


    private static void moveImage(String foldername, String imageName) {


        File sourceFile = new File("src/main/resources/static/pref/" + foldername +"/" +imageName);
        File targetFile = new File("src/main/resources/static/images/" + imageName);

        if (sourceFile.exists()) {
            try {
                // Move file from source to target folder
                Path sourcePath = Paths.get(sourceFile.getPath());
                Path targetPath = Paths.get(targetFile.getPath());
                Files.copy(sourcePath, targetPath);

                System.out.println("File moved successfully.");
            } catch (IOException e) {
                System.err.println("Failed to move file: " + e.getMessage());
            }
        } else {
            System.out.println("File not found.");
        }
    }

    @RequestMapping(value = "/profiles", method = GET)
    public String profile(Model model) {

        if(SessionSingleton.getInstance().getUsername()!=null &&
                !SessionSingleton.getInstance().getUsername().isEmpty()){
  // List all user profiles in profilelist : list datastructure
         List<Profile> profileList =  profileRepository.findByUserName(SessionSingleton.getInstance().getUsername());

            model.addAttribute("username", SessionSingleton.getInstance().getUsername());
            model.addAttribute("propref",profileList);
            return "profiles";
        }
        return "login";
    }



    @RequestMapping(value = "/homemenu", method = GET)
    public String homemenu(Model model) {

        if(SessionSingleton.getInstance().getUsername()!=null &&
                !SessionSingleton.getInstance().getUsername().isEmpty()){
            model.addAttribute("username", SessionSingleton.getInstance().getUsername());
            return "homemenu";
        }
        return "login";
    }

    @RequestMapping(value = "/addProfile", method = GET)
    public String addProfile(Model model) {

        if(SessionSingleton.getInstance().getUsername()!=null &&
                !SessionSingleton.getInstance().getUsername().isEmpty()){


            model.addAttribute("username", SessionSingleton.getInstance().getUsername());
            return "addprofile";
        }
        return "login";
    }

    @PostMapping("/insert")
    public String insert(@ModelAttribute CustomFields custom, Model model) {


        Preferences pref = new Preferences();
        pref.setBed(custom.getBed());
        pref.setTv(custom.getTv());
        pref.setComputer(custom.getComputer());
        pref.setLamp(custom.getLamp());
        pref.setCouch(custom.getCouch());
        pref.setChair(custom.getChair());
        pref.setCupboard(custom.getCupboard());
        pref.setFridge(custom.getFridge());
        pref.setMicrowave(custom.getMicrowave());
        pref.setMirror(custom.getMirror());
        pref.setRecliner(custom.getRecliner());
        pref.setWardrobe(custom.getWardrobe());
        pref.setTheme(custom.getTheme());
        pref.setMessageBedroom(custom.getMessageBedroom());
        pref.setMessageKitchen(custom.getMessageKitchen());

        pref.setPrefName(custom.getPreferenceName());


        pref.setUserName(SessionSingleton.getInstance().getUsername());
        pref.setProfileName(SessionSingleton.getInstance().getActiveProfileName());

        prefRepo.save(pref);

        List<Preferences> preferencesList = prefRepo.findByUserNameAndProfileName(SessionSingleton.getInstance().getUsername(),
                SessionSingleton.getInstance().getActiveProfileName());


        model.addAttribute("profileName", SessionSingleton.getInstance().getActiveProfileName());
        model.addAttribute("preferencesList",  preferencesList);

        return "profile";
    }

    @RequestMapping(value = "/addProfile", method = POST)
    public String addProfileEnd(Model model, @ModelAttribute ProfileForm profileForm) {

        if(SessionSingleton.getInstance().getUsername()!=null &&
                !SessionSingleton.getInstance().getUsername().isEmpty()){

          String username =  SessionSingleton.getInstance().getUsername();
          String profileName = profileForm.getProfilename();

          Profile profileDB = new Profile();
          profileDB.setProfileName(profileName);
          profileDB.setUserName(username);
          profileDB.setPlayedBedroom(0);
          profileDB.setPlayedKitchen(0);
          profileRepository.save(profileDB);

          Preferences pref = new Preferences();
          //Adding objects

          pref.setBed(profileForm.getBed());
          pref.setTv(profileForm.getTv());
          pref.setComputer(profileForm.getComputer());
          pref.setLamp(profileForm.getLamp());
          pref.setCouch(profileForm.getCouch());
          pref.setChair(profileForm.getChair());
          pref.setCupboard(profileForm.getCupboard());
          pref.setFridge(profileForm.getFridge());
          pref.setMicrowave(profileForm.getMicrowave());
          pref.setMirror(profileForm.getMirror());
          pref.setRecliner(profileForm.getRecliner());
          pref.setWardrobe(profileForm.getWardrobe());
          pref.setTheme(profileForm.getTheme());
          pref.setMessageBedroom(profileForm.getMessageBedroom());
          pref.setMessageKitchen(profileForm.getMessageKitchen());

          pref.setPrefName(profileForm.getPrefname());
          pref.setProfileName(profileName);
          pref.setUserName(username);

          prefRepo.save(pref);

            List<Profile> profileList =  profileRepository.findByUserName(SessionSingleton.getInstance().getUsername());

            model.addAttribute("username", SessionSingleton.getInstance().getUsername());
            model.addAttribute("propref",profileList);
            return "profiles";
        }
        return "login";
    }

    //access get parameter from frontend to backend via link we are using path variable
    @GetMapping("/profile/{profileName}")
    public String individualProfile(@PathVariable String profileName, Model model) {

        if(SessionSingleton.getInstance().getUsername()!=null &&
                !SessionSingleton.getInstance().getUsername().isEmpty()){

            SessionSingleton.getInstance().setActiveProfileName(profileName);

           List<Preferences> preferencesList = prefRepo.findByUserNameAndProfileName(SessionSingleton.getInstance().getUsername(), profileName);


            model.addAttribute("profileName", SessionSingleton.getInstance().getActiveProfileName());
            model.addAttribute("preferencesList",  preferencesList);

            return "profile";
        }
        return "login";
    }

    @GetMapping("/deleteProfile/{profileName}")
    public String deleteProfile(@PathVariable String profileName, Model model) {

        if(SessionSingleton.getInstance().getUsername()!=null &&
                !SessionSingleton.getInstance().getUsername().isEmpty()){

            prefRepo.deleteByUserNameAndProfileName(SessionSingleton.getInstance().getUsername(),profileName);

            profileRepository.deleteByUserNameAndProfileName(SessionSingleton.getInstance().getUsername(),profileName);

            List<Profile> profileList =  profileRepository.findByUserName(SessionSingleton.getInstance().getUsername());

            model.addAttribute("username", SessionSingleton.getInstance().getUsername());
            model.addAttribute("propref",profileList);

            return "profiles";
        }
        return "login";
    }

    @GetMapping("/deletePreference/{preferenceName}")
    public String deletePreference(@PathVariable String preferenceName, Model model) {

        if(SessionSingleton.getInstance().getUsername()!=null &&
                !SessionSingleton.getInstance().getUsername().isEmpty()){

            String profileName =  SessionSingleton.getInstance().getActiveProfileName();

            long prefCount = prefRepo.countByProfileName(profileName);
            if(prefCount > 1) {
                prefRepo.deleteByUserNameAndProfileNameAndPrefName(SessionSingleton.getInstance().getUsername(),
                        profileName, preferenceName);
            }else{
                model.addAttribute("errorPref", "Profiles need to have at least one preference to launch the game");
            }
                List<Preferences> preferencesList = prefRepo.findByUserNameAndProfileName(SessionSingleton.getInstance().getUsername(),
                        profileName);

                model.addAttribute("profileName", SessionSingleton.getInstance().getActiveProfileName());
                model.addAttribute("preferencesList", preferencesList);

                return "profile";
        }
        return "login";
    }
}

