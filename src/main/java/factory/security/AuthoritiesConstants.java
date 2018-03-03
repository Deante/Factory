package factory.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String GESTIONNAIRE = "ROLE_GESTIONNAIRE";

    public static final String FORMATEUR = "ROLE_FORMATEUR";

    public static final String STAGIAIRE = "ROLE_STAGIAIRE";

    public static final String TECHNICIEN = "ROLE_TECHNICIEN";

    public static final String USER = "ROLE_USER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    private AuthoritiesConstants() {
    }
}
