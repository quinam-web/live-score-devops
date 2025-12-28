package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;

import java.util.*;

@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}

// ================= MODELS =================

class Match {
    public int id;
    public String homeTeam;
    public String awayTeam;
    public int homeScore;
    public int awayScore;
    public String time; 
    public boolean isLive;
    public String homeLogo; 
    public String awayLogo; 

    public Match(int id, String homeTeam, String awayTeam, int homeScore, int awayScore, String time, boolean isLive, String homeLogo, String awayLogo) {
        this.id = id;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.time = time;
        this.isLive = isLive;
        this.homeLogo = homeLogo;
        this.awayLogo = awayLogo;
    }
}

class Fixture {
    public String opponent;
    public String date;
    public String logo;
    public String type; 

    public Fixture(String opponent, String date, String logo, String type) {
        this.opponent = opponent;
        this.date = date;
        this.logo = logo;
        this.type = type;
    }
}

class RecentResult {
    public String result; 
    public String opponent;
    public String score;

    public RecentResult(String result, String opponent, String score) {
        this.result = result;
        this.opponent = opponent;
        this.score = score;
    }
}

class Lineup {
    public String teamName;
    public String manager;
    public List<String> startingXI;
    public List<String> substitutes;
    public String formation;

    public Lineup(String teamName, String manager, List<String> startingXI, List<String> substitutes, String formation) {
        this.teamName = teamName;
        this.manager = manager;
        this.startingXI = startingXI;
        this.substitutes = substitutes;
        this.formation = formation;
    }
}

// ================= SERVICE =================

@Service
class LiveScoreService {
    
    private static final Map<String, String> LOGO_MAP = new HashMap<>();
    static {
        LOGO_MAP.put("Man United", "https://resources.premierleague.com/premierleague/badges/50/t1.png");
        LOGO_MAP.put("Chelsea", "https://resources.premierleague.com/premierleague/badges/50/t8.png");
        LOGO_MAP.put("Arsenal", "https://resources.premierleague.com/premierleague/badges/50/t3.png");
        LOGO_MAP.put("Liverpool", "https://resources.premierleague.com/premierleague/badges/50/t14.png");
        LOGO_MAP.put("Man City", "https://resources.premierleague.com/premierleague/badges/50/t43.png");
        LOGO_MAP.put("Tottenham", "https://resources.premierleague.com/premierleague/badges/50/t6.png");
        LOGO_MAP.put("Newcastle", "https://resources.premierleague.com/premierleague/badges/50/t4.png");
        LOGO_MAP.put("Aston Villa", "https://resources.premierleague.com/premierleague/badges/50/t7.png");
        LOGO_MAP.put("West Ham", "https://resources.premierleague.com/premierleague/badges/50/t21.png");
        LOGO_MAP.put("Brighton", "https://resources.premierleague.com/premierleague/badges/50/t36.png");
        LOGO_MAP.put("Everton", "https://resources.premierleague.com/premierleague/badges/50/t11.png");
        LOGO_MAP.put("Crystal Palace", "https://resources.premierleague.com/premierleague/badges/50/t31.png");
        LOGO_MAP.put("Fulham", "https://resources.premierleague.com/premierleague/badges/50/t54.png");
        LOGO_MAP.put("Brentford", "https://resources.premierleague.com/premierleague/badges/50/t94.png");
        LOGO_MAP.put("Wolves", "https://resources.premierleague.com/premierleague/badges/50/t39.png");
        LOGO_MAP.put("Luton", "https://resources.premierleague.com/premierleague/badges/50/t102.png");
        LOGO_MAP.put("Nottm Forest", "https://resources.premierleague.com/premierleague/badges/50/t17.png");
        LOGO_MAP.put("Bournemouth", "https://resources.premierleague.com/premierleague/badges/50/t91.png");
        LOGO_MAP.put("Sheffield Utd", "https://resources.premierleague.com/premierleague/badges/50/t49.png");
    }

    public String getLogoUrl(String teamName) {
        return LOGO_MAP.getOrDefault(teamName, "https://placehold.co/40x40/cccccc/000000?text=PL");
    }

    private List<Match> matches = new ArrayList<>();

    public LiveScoreService() {
        matches.add(new Match(1, "Man United", "Chelsea", 0, 0, "LIVE", true, getLogoUrl("Man United"), getLogoUrl("Chelsea")));
        matches.add(new Match(2, "Arsenal", "Liverpool", 0, 0, "LIVE", true, getLogoUrl("Arsenal"), getLogoUrl("Liverpool")));
        matches.add(new Match(3, "Man City", "Tottenham", 3, 1, "FT", false, getLogoUrl("Man City"), getLogoUrl("Tottenham")));
        matches.add(new Match(4, "Newcastle", "Aston Villa", 0, 0, "20:00", false, getLogoUrl("Newcastle"), getLogoUrl("Aston Villa")));
    }

    public List<Match> getLiveMatches() {
        Random rand = new Random();
        for (Match m : matches) {
            if (m.isLive) {
                m.homeScore = rand.nextInt(4);
                m.awayScore = rand.nextInt(3);
                m.time = "LIVE " + (15 + rand.nextInt(75)) + "'";
            }
        }
        return matches;
    }

    public Match getMatchById(int id) {
        return matches.stream().filter(m -> m.id == id).findFirst().orElse(null);
    }

    public Lineup getLineupByTeam(String teamName) {
        switch (teamName) {
            case "Man United":
                return new Lineup("Man United", "Ruben Amorim", 
                    Arrays.asList("Onana", "Dalot", "De Ligt", "Martinez", "Mazraoui", "Casemiro", "Ugarte", "Garnacho", "Fernandes", "Rashford", "Hojlund"),
                    Arrays.asList("Bayindir", "Maguire", "Evans", "Eriksen", "Mount", "Amad", "Zirkzee"), "3-4-3");
            
            case "Chelsea":
                return new Lineup("Chelsea", "Enzo Maresca",
                    Arrays.asList("Sanchez", "Gusto", "Fofana", "Colwill", "Cucurella", "Caicedo", "Lavia", "Madueke", "Palmer", "Neto", "Jackson"),
                    Arrays.asList("Jorgensen", "Adarabioyo", "Veiga", "Enzo", "Mudryk", "Felix", "Nkunku"), "4-2-3-1");
            
            case "Arsenal":
                return new Lineup("Arsenal", "Mikel Arteta",
                    Arrays.asList("Raya", "White", "Saliba", "Gabriel", "Timber", "Partey", "Rice", "Merino", "Saka", "Havertz", "Martinelli"),
                    Arrays.asList("Neto", "Calafiori", "Zinchenko", "Jorginho", "Odegaard", "Trossard", "Jesus"), "4-3-3");
            
            case "Liverpool":
                return new Lineup("Liverpool", "Arne Slot",
                    Arrays.asList("Kelleher", "Alexander-Arnold", "Konate", "Van Dijk", "Robertson", "Gravenberch", "Mac Allister", "Salah", "Jones", "Diaz", "Nunez"),
                    Arrays.asList("Jaros", "Quansah", "Gomez", "Tsimikas", "Szoboszlai", "Gakpo", "Jota"), "4-2-3-1");
            
            case "Man City":
                return new Lineup("Man City", "Pep Guardiola",
                    Arrays.asList("Ederson", "Walker", "Akanji", "Dias", "Gvardiol", "Rodri", "Kovacic", "Bernardo", "Foden", "Doku", "Haaland"),
                    Arrays.asList("Ortega", "Stones", "Ake", "Lewis", "Gundogan", "De Bruyne", "Savinho"), "4-1-4-1");
            
            case "Tottenham":
                return new Lineup("Tottenham", "Ange Postecoglou",
                    Arrays.asList("Vicario", "Porro", "Romero", "Van de Ven", "Udogie", "Sarr", "Bissouma", "Maddison", "Kulusevski", "Son", "Solanke"),
                    Arrays.asList("Forster", "Dragusin", "Gray", "Bentancur", "Bergvall", "Werner", "Johnson"), "4-3-3");
            
            case "Newcastle":
                return new Lineup("Newcastle", "Eddie Howe",
                    Arrays.asList("Pope", "Livramento", "Schar", "Burn", "Hall", "Tonali", "Guimaraes", "Joelinton", "Gordon", "Barnes", "Isak"),
                    Arrays.asList("Dubravka", "Krafth", "Kelly", "Longstaff", "Willock", "Almiron", "Wilson"), "4-3-3");
            
            case "Aston Villa":
                return new Lineup("Aston Villa", "Unai Emery",
                    Arrays.asList("Martinez", "Cash", "Konsa", "Torres", "Digne", "Onana", "Tielemans", "Bailey", "Rogers", "Ramsey", "Watkins"),
                    Arrays.asList("Olsen", "Carlos", "Maatsen", "Barkley", "Kamara", "Buendia", "Duran"), "4-2-3-1");
            
            default:
                return new Lineup(teamName, "Head Coach",
                    Arrays.asList("GK Player", "Defender 1", "Defender 2", "Defender 3", "Defender 4", "Mid 1", "Mid 2", "Mid 3", "Att 1", "Att 2", "Att 3"),
                    Arrays.asList("Sub 1", "Sub 2", "Sub 3", "Sub 4", "Sub 5", "Sub 6"), "4-4-2");
        }
    }

    // CẬP NHẬT: Logic lịch thi đấu riêng biệt cho từng đội
    public List<Fixture> getNext3Fixtures(String teamName) {
        List<Fixture> fixtures = new ArrayList<>();
        
        switch (teamName) {
            case "Man United":
                fixtures.add(new Fixture("Nottm Forest", "07/12", getLogoUrl("Nottm Forest"), "(H)"));
                fixtures.add(new Fixture("Man City", "15/12", getLogoUrl("Man City"), "(A)"));
                fixtures.add(new Fixture("Bournemouth", "22/12", getLogoUrl("Bournemouth"), "(H)"));
                break;
                
            case "Chelsea":
                fixtures.add(new Fixture("Everton", "07/12", getLogoUrl("Everton"), "(A)"));
                fixtures.add(new Fixture("Sheffield Utd", "16/12", getLogoUrl("Sheffield Utd"), "(H)"));
                fixtures.add(new Fixture("Wolves", "24/12", getLogoUrl("Wolves"), "(A)"));
                break;
                
            case "Arsenal":
                fixtures.add(new Fixture("Aston Villa", "09/12", getLogoUrl("Aston Villa"), "(A)"));
                fixtures.add(new Fixture("Brighton", "17/12", getLogoUrl("Brighton"), "(H)"));
                fixtures.add(new Fixture("Liverpool", "23/12", getLogoUrl("Liverpool"), "(A)"));
                break;
                
            case "Liverpool":
                fixtures.add(new Fixture("Crystal Palace", "09/12", getLogoUrl("Crystal Palace"), "(A)"));
                fixtures.add(new Fixture("Man United", "17/12", getLogoUrl("Man United"), "(H)"));
                fixtures.add(new Fixture("Arsenal", "23/12", getLogoUrl("Arsenal"), "(H)"));
                break;
                
            case "Man City":
                fixtures.add(new Fixture("Luton", "10/12", getLogoUrl("Luton"), "(A)"));
                fixtures.add(new Fixture("Crystal Palace", "16/12", getLogoUrl("Crystal Palace"), "(H)"));
                fixtures.add(new Fixture("Everton", "27/12", getLogoUrl("Everton"), "(A)"));
                break;
                
            case "Tottenham":
                fixtures.add(new Fixture("Newcastle", "10/12", getLogoUrl("Newcastle"), "(H)"));
                fixtures.add(new Fixture("Nottm Forest", "15/12", getLogoUrl("Nottm Forest"), "(A)"));
                fixtures.add(new Fixture("Everton", "23/12", getLogoUrl("Everton"), "(H)"));
                break;
                
            case "Newcastle":
                fixtures.add(new Fixture("Tottenham", "10/12", getLogoUrl("Tottenham"), "(A)"));
                fixtures.add(new Fixture("Fulham", "16/12", getLogoUrl("Fulham"), "(H)"));
                fixtures.add(new Fixture("Luton", "23/12", getLogoUrl("Luton"), "(A)"));
                break;
                
            case "Aston Villa":
                fixtures.add(new Fixture("Arsenal", "09/12", getLogoUrl("Arsenal"), "(H)"));
                fixtures.add(new Fixture("Brentford", "17/12", getLogoUrl("Brentford"), "(A)"));
                fixtures.add(new Fixture("Sheffield Utd", "22/12", getLogoUrl("Sheffield Utd"), "(H)"));
                break;
                
            default:
                fixtures.add(new Fixture("West Ham", "08/12", getLogoUrl("West Ham"), "(H)"));
                fixtures.add(new Fixture("Brighton", "15/12", getLogoUrl("Brighton"), "(A)"));
                fixtures.add(new Fixture("Fulham", "22/12", getLogoUrl("Fulham"), "(H)"));
                break;
        }
        return fixtures;
    }

    public List<RecentResult> getRecentForm(String teamName) {
        List<RecentResult> form = new ArrayList<>();
        // Logic phong độ riêng biệt
        String[] results;
        if (Arrays.asList("Man City", "Liverpool", "Arsenal").contains(teamName)) {
            results = new String[]{"W", "W", "W", "D", "W"}; 
        } else if (Arrays.asList("Man United", "Chelsea", "Tottenham").contains(teamName)) {
            results = new String[]{"W", "L", "D", "W", "L"}; 
        } else {
            results = new String[]{"L", "D", "L", "W", "L"}; 
        }
        
        String[] opponents = {"Everton", "Luton", "Wolves", "Brentford", "Burnley"};
        for (int i = 0; i < 5; i++) {
            form.add(new RecentResult(results[i], opponents[i], "2-1"));
        }
        return form;
    }
}

// ================= CONTROLLER =================

@Controller
class ScoreController {
    private final LiveScoreService liveScoreService;

    public ScoreController(LiveScoreService liveScoreService) {
        this.liveScoreService = liveScoreService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("matches", liveScoreService.getLiveMatches());
        return "index";
    }

    @GetMapping("/match/{id}")
    public String matchDetail(@PathVariable int id, Model model) {
        Match match = liveScoreService.getMatchById(id);
        
        if (match != null) {
            model.addAttribute("match", match);
            
            // Lấy dữ liệu chi tiết
            model.addAttribute("homeLineup", liveScoreService.getLineupByTeam(match.homeTeam));
            model.addAttribute("awayLineup", liveScoreService.getLineupByTeam(match.awayTeam));
            
            model.addAttribute("homeFixtures", liveScoreService.getNext3Fixtures(match.homeTeam));
            model.addAttribute("awayFixtures", liveScoreService.getNext3Fixtures(match.awayTeam));

            model.addAttribute("homeForm", liveScoreService.getRecentForm(match.homeTeam));
            model.addAttribute("awayForm", liveScoreService.getRecentForm(match.awayTeam));
        }
        
        return "match-detail"; 
    }
}