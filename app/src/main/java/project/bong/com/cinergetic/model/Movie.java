package project.bong.com.cinergetic.model;

public class Movie  {
    String title;       // 영화명
    String prodYear;    // 제작연도
    String directorNm;  // 감독
    String actorNm;     // 출연진
    String nation;      // 상영국가
    String company;     // 상영사
    String plot;        // 줄거리
    String runtime;     // 상영시간
    String rating;      // 상영등급
    String genre;       // 장르
    String url;         // 영화포스터 (네이버API에서 가져오는 데이터)

    public Movie(){
        title = prodYear = directorNm = actorNm = nation
                = company = plot = runtime = rating = genre = url = "";
    }

    public Movie(String title, String prodYear, String directorNm, String actorNm, String nation,
                 String company, String plot, String runtime, String rating, String url, String genre) {
        this.title = title;
        this.prodYear = prodYear;
        this.directorNm = directorNm;
        this.actorNm = actorNm;
        this.nation = nation;
        this.company = company;
        this.plot = plot;
        this.runtime = runtime;
        this.rating = rating;
        this.genre = genre;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProdYear() {
        return prodYear;
    }

    public void setProdYear(String prodYear) {
        this.prodYear = prodYear;
    }

    public String getDirectorNm() {
        return directorNm;
    }

    public void setDirectorNm(String directorNm) {
        this.directorNm = directorNm;
    }

    public String getActorNm() {
        return actorNm;
    }

    public void setActorNm(String actorNm) {
        this.actorNm = actorNm;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
