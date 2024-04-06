package ca.csfoy.tp2_ac_ltn.database;

public class GameGoTable {
    //Supprime la database completement
    public static final String DROP_TABLE_SQL = "Drop table Play";
    //Creer la database
    public static final String CREATE_TABLE_SQL = "" +
            "CREATE TABLE IF NOT EXISTS Play("+
            "  no_play         INTEGER       PRIMARY KEY    AUTOINCREMENT,"+
            "  color           TEXT,"+
            "  stone           TEXT"+
            ")";
    //Selectionne un Play par le numero
    public static final String SELECT_ONE_OF_SQL="SELECT * FROM Play WHERE no_play = $no_play";
    //Selectionne toute la table
    public static final String SELECT_ALL_OF_SQL = " SELECT* FROM Play";
    //Selectionne le Move avec le NO_PLAY le plus haut
    public static final String SELECT_LAST_OF_SQL = ""+
            "SELECT"+
            "MAX(no_play),"+
            "Play.color,"+
            "Play.stone"+
            "FROM"+
            "Play";
    //Permet d'inserer dans la DB de nouvel donnes et autoincrementation du NO_PLAY
    public static final String INSERT_SQL=""+
            "INSERT INTO Play ( "+
            "  color,"+
            "  stone"+
            ")VALUES("+
            "  $color,"+
            "  $stone"+
            ")";
    //Permet d'updater un move par rapport a son NO_PLAY
    public static final String UPDATE_SQL=""+
            "UPDATE Play"+
            "SET"+
            "  color = $color,"+
            "  stone = $stone"+
            "WHERE"+
            "no_play = $no_play";
    //Selectionne un NO_PLAY random PS: je sais pas si on va en avoir besoin
    public static final String SELECT_SQL_WITH_RANDOM_ID= "SELECT * FROM Play ORDER BY RANDOM() LIMIT 1;";
    //Supprime juste un MOVE de la BD
    public static final String DELETE_SQL = ""+
            "DELETE FROM Play"+
            "WHERE"+
            "  no_play = $no_play";
    //Supprime tout les moves dans la BD
    public static final String DELETE_ALL_SQL = ""+
            "DELETE * FROM Play";
}
