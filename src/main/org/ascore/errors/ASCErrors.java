package org.ascore.errors;

import org.ascore.executor.ASCExecutor;
import org.ascore.managers.data.Data;

/**
 * Interface des erreurs d'AliveScript
 *
 * @author Mathis
 */
public interface ASCErrors {

    class ASCError extends RuntimeException {
        private final String nomErreur;

        public ASCError(String message, String nomErreur) {
            super(message);
            this.nomErreur = nomErreur;
        }

        public Data getAsData(ASCExecutor<?> executorInstance) {
            int ligne = executorInstance.getLineFromCoord(executorInstance.obtenirCoordRunTime()) + 1;
            return new Data(Data.Id.ERREUR).addParam(nomErreur).addParam(super.getMessage()).addParam(ligne);
        }

        public Data getAsData(int ligne) {
            return new Data(Data.Id.ERREUR).addParam(nomErreur).addParam(super.getMessage()).addParam(ligne);
        }

        public void afficher(ASCExecutor<?> executorInstance) {
            int ligne = executorInstance.getLineFromCoord(executorInstance.obtenirCoordRunTime()) + 1;
            executorInstance.ecrire(this.nomErreur + " (à la ligne " + ligne
                                    + ") -> " + super.getMessage());
        }

        public void afficher(ASCExecutor<?> executorInstance, int ligne) {
            executorInstance.ecrire(this.nomErreur + " (à la ligne " + ligne
                                    + ") -> " + super.getMessage());
        }

        public String getNomErreur() {
            return nomErreur;
        }
    }

    class Stop extends RuntimeException {
        private final Data data;

        public Stop(Data data) {
            this.data = data;
        }

        public Data getData() {
            return data;
        }
    }

    class StopSendData extends RuntimeException {
        private final String dataString;

        public StopSendData(String dataString) {
            this.dataString = dataString;
        }

        public String getDataString() {
            return dataString;
        }
    }

    class StopGetInfo extends Stop {
        public StopGetInfo(Data data) {
            super(data);
        }
    }

    //--------------------------------------Erreurs compilation----------------------------------------------

    class StopSetInfo extends Stop {
        public StopSetInfo(Data data) {
            super(data);
        }
    }


    //-----------------------------------------------Erreurs Executions-------------------------------------------

    class ErreurFermeture extends ASCError {

        public ErreurFermeture(String blocActuel) {
            super("le bloc: '" + blocActuel + "' n'a pas été fermé.", "ErreurFermeture");
        }

        public ErreurFermeture(String blocActuel, String mauvaiseFermeture) {
            super("le bloc: '" + blocActuel + "' a été fermé avec '"
                    + mauvaiseFermeture + "' alors qu'il ne peut pas être fermé.", "ErreurFermeture");
        }

        public ErreurFermeture(String blocActuel, String mauvaiseFermeture, String bonneFermeture) {
            super("le bloc: '" + blocActuel + "' a été fermé avec '"
                    + mauvaiseFermeture + "' alors qu'il doit être fermé avec '" + bonneFermeture + "'.", "ErreurFermeture");
        }
    }

    class ErreurContexteAbsent extends ASCError {
        public ErreurContexteAbsent(String message) {
            super(message, "ErreurContexteAbsent");
        }
    }

    class ErreurSyntaxe extends ASCError {
        public ErreurSyntaxe(String message) {
            super(message, "ErreurSyntaxe");
        }
    }

    class ErreurModule extends ASCError {
        public ErreurModule(String message) {
            super(message, "ErreurModule");
        }
    }

    class ErreurAppelFonction extends ASCError {
        public ErreurAppelFonction(String message) {
            super(message, "ErreurAppelFonction");
        }

        public ErreurAppelFonction(String nom, String message) {
            super("dans la fonction '" + nom + "': " + message, "ErreurAppelFonction");
        }
    }

    class ErreurBoucleInfini extends ASCError {
        public ErreurBoucleInfini(String message) {
            super(message, "ErreurBoucleInfini");
        }
    }

    class ErreurInputOutput extends ASCError {
        public ErreurInputOutput(String message) {
            super(message, "ErreurInputOutput");
        }
    }

    class ErreurAssignement extends ASCError {
        public ErreurAssignement(String message) {
            super(message, "ErreurAssignement");
        }
    }

    class ErreurDeclaration extends ASCError {
        public ErreurDeclaration(String message) {
            super(message, "ErreurDeclaration");
        }
    }

    class ErreurType extends ASCError {
        public ErreurType(String message) {
            super(message, "ErreurType");
        }
    }

    class ErreurClef extends ASCError {
        public ErreurClef(String clef) {
            super("La clef " + clef + " n'est pas pr\u00E9sente dans le dict ou la liste", "ErreurClef");
        }
    }

    class ErreurClefDupliquee extends ASCError {
        public ErreurClefDupliquee(String msg) {
            super(msg, "ErreurClef");
        }
    }

    class ErreurIndex extends ASCError {
        public ErreurIndex(String message) {
            super(message, "ErreurIndex");
        }
    }

    class ErreurVariableInconnue extends ASCError {
        public ErreurVariableInconnue(String message) {
            super(message, "ErreurVariableInconnue");
        }
    }

    class ErreurComparaison extends ASCError {
        public ErreurComparaison(String message) {
            super(message, "ErreurComparaison");
        }
    }

    class ErreurFormatage extends ASCError {
        public ErreurFormatage(String message) {
            super(message, "ErreurFormatage");
        }
    }

    class ErreurSuite extends ASCError {
        public ErreurSuite(String message) {
            super(message, "ErreurSuite");
        }
    }


    //-------------------------  Erreur de mathématiques  -----------------------------//

    class ErreurEntierInvalide extends ASCError {
        public ErreurEntierInvalide(String message) {
            super(message, "ErreurEntierInvalide");
        }
    }

    class ErreurArithmetique extends ASCError {
        public ErreurArithmetique(String message) {
            super(message, "ErreurArithmetique");
        }
    }

    class ErreurDivisionParZero extends ASCError {
        public ErreurDivisionParZero(String message) {
            super(message, "ErreurDivisionParZero");
        }
    }

    class ErreurModuloZero extends ASCError {
        public ErreurModuloZero(String message) {
            super(message, "ErreurModuloZero");
        }
    }

    class ErreurZeroExposantZero extends ASCError {
        public ErreurZeroExposantZero(String message) {
            super(message, "ErreurExposantZero");
        }
    }


    // ----------------------------------- Alertes ----------------------------------------- //

    class AlerteExecution {
        private final String message;

        public AlerteExecution(String message) {
            this.message = message;
        }

        public void afficher(ASCExecutor<?> executorInstance) {
            int ligne = executorInstance.getLineFromCoord(executorInstance.obtenirCoordRunTime()) + 1;
            executorInstance.ecrire("Durant l'execution à la ligne " + ligne
                                    + " -> " + this.getClass().getSimpleName() + " : " + this.message);
        }
    }


    class AlerteUtiliserBuiltins extends AlerteExecution {
        public AlerteUtiliserBuiltins(String message) {
            super(message);
        }
    }

}





























