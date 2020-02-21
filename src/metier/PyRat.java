package metier;

import java.util.*;

public class PyRat {
    private HashSet<Point> fromages_set;
    private HashMap<Point, HashSet<Point>> de_a;
    private Map<Point, Set<Point>> routes;

    /* Méthode appelée une seule fois permettant d'effectuer des traitements "lourds" afin d'augmenter la performace de la méthode turn. */
    public void preprocessing(Map<Point, List<Point>> laby, int labyWidth, int labyHeight, Point position, List<Point> fromages) {
        fromages_set = new HashSet<>(fromages);
        de_a = new HashMap<>();

        for (Point point: laby.keySet()) {
            de_a(point, laby, new HashSet<>());
        }

    }

    public void de_a(Point de, Map<Point, List<Point>> laby, Set<Point> visited) {
        ArrayList<Point> points = new ArrayList<>();
        Point point;
        points.add(de);
        visited.add(de);
        HashSet<Point> set = new HashSet<>();
        de_a.put(de, set);

        while (!points.isEmpty()) {
            point = points.remove(0);
            set.add(point);

            for (Point voisin: laby.get(point)) {
                if (!visited.contains(voisin)) {
                    visited.add(voisin);
                    points.add(voisin);
                }
            }
        }
    }

    /* Méthode de test appelant les différentes fonctionnalités à développer.
        @param laby - Map<Point, List<Point>> contenant tout le labyrinthe, c'est-à-dire la liste des Points, et les Points en relation (passages existants)
        @param labyWidth, labyHeight - largeur et hauteur du labyrinthe
        @param position - Point contenant la position actuelle du joueur
        @param fromages - List<Point> contenant la liste de tous les Points contenant un fromage. */
    public void turn(Map<Point, List<Point>> laby, int labyWidth, int labyHeight, Point position, List<Point> fromages) {
        Point pt1 = new Point(2,1);
        Point pt2 = new Point(3,1);

        System.out.println((fromageIci(pt1, fromages) ? "Il y a un" : "Il n'y a pas de") + " fromage ici, en position " + pt1);
        System.out.println((fromageIci_EnOrdreConstant(pt2) ? "Il y a un" : "Il n'y a pas de") + " fromage ici, en position " + pt2);
        System.out.println((passagePossible(pt1, pt2, laby, new HashSet<>()) ? "Il y a un" : "Il n'y a pas de") + " passage de " + pt1 + " vers " + pt2);
        System.out.println((passagePossible_EnOrdreConstant(pt1, pt2) ? "Il y a un" : "Il n'y a pas de") + " passage de " + pt1 + " vers " + pt2);
        System.out.println("Liste des points inatteignables depuis la position " + position + " : " + pointsInatteignables(position, laby));
    }

    /* Regarde dans la liste des fromages s’il y a un fromage à la position pos.
        @return true s'il y a un fromage à la position pos, false sinon. */
    private boolean fromageIci(Point pos, List<Point> fromages) {
        for (Point point: fromages) {
            if (pos.equals(point)) {
                return true;
            }
        }
        return false;
    }

    /* Regarde de manière performante (accès en ordre constant) s’il y a un fromage à la position pos.
        @return true s'il y a un fromage à la position pos, false sinon. */
    private boolean fromageIci_EnOrdreConstant(Point pos) {
        return fromages_set.contains(pos);
    }

    /* Indique si le joueur peut passer de la position (du Point) « de » au point « a ».
        @return true s'il y a un passage depuis  « de » vers « a ». */
    private boolean passagePossible(Point de, Point a, Map<Point, List<Point>> laby, Set<Point> visited) {
        ArrayList<Point> points = new ArrayList<>();
        Point point;
        points.add(de);
        visited.add(de);

        while (!points.isEmpty()) {
            point = points.remove(0);

            if (laby.get(point).contains(a)) {
                return true;
            }

            for (Point voisin: laby.get(point)) {
                if (!visited.contains(voisin)) {
                    visited.add(voisin);
                    points.add(voisin);
                }
            }
        }
        return false;
    }

    /* Indique si le joueur peut passer de la position (du Point) « de » au point « a »,
        mais sans devoir parcourir la liste des Points se trouvant dans la Map !
        @return true s'il y a un passage depuis  « de » vers « a ». */
    private boolean passagePossible_EnOrdreConstant(Point de, Point a) {
        return de_a.get(de).contains(a);
    }

    /* Retourne la liste des points qui ne peuvent pas être atteints depuis la position « pos ».
        @return la liste des points qui ne peuvent pas être atteints depuis la position « pos ». */
    private List<Point> pointsInatteignables(Point pos, Map<Point, List<Point>> laby) {
        List<Point> non_points = new ArrayList<>();

        for (Point point: laby.keySet()) {
            if (!de_a.get(pos).contains(point)) {
                non_points.add(point);
            }
        }

        return non_points;
    }
}