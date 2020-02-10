package com.jdemandre.instartist.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.jdemandre.instartist.Model.Publication;
import com.jdemandre.instartist.R;

/**
 * Implementation of App Widget functionality.
 */
public class InstartistWidget extends AppWidgetProvider {

    // Les tutos que propose notre widget
    private final static Publication PUBLICATIONS[] = {
            new Publication("1", "Une image URL", "Une premiere description", "Romain"),
            new Publication("2", "/azjhSKLUSJF:AEZER", "Photo du Japon", "Romain"),
            new Publication("3", "/depppe/qsdqq.png", "Un oiseau dans le ciel", "Steeve"),
            new Publication("4", "/photo/1.jpg", "Une premiere description", "Lili"),

    };

    // Intitulé de l'extra qui contient la direction du défilé
    private final static String EXTRA_DIRECTION = "extraDirection";

    // La valeur pour défiler vers la gauche
    private final static String EXTRA_PREVIOUS = "previous";

    // La valeur pour défiler vers la droite
    private final static String EXTRA_NEXT = "next";

    // Intitulé de l'extra qui contient l'indice actuel dans le tableau des tutos
    private final static String EXTRA_INDICE = "extraIndice";

    // Action qui indique qu'on essaie d'ouvrir un tuto sur internet
    private final static String ACTION_OPEN_TUTO = "sdz.chapitreQuatre.tutowidget.action.OPEN_TUTO";

    // Indice actuel dans le tableau des tutos
    private int indice = 0;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        // Petite astuce : permet de garder la longueur du tableau sans accéder plusieurs fois à l'objet, d'où optimisation
        final int length = appWidgetIds.length;
        for (int i = 0 ; i < length ; i++) {
            // On récupère le RemoteViews qui correspond à l'AppWidget
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.instartist_widget);

            // On met le bon texte dans le bouton
            views.setTextViewText(R.id.link, PUBLICATIONS[indice].getDescription());

            // La prochaine section est destinée au bouton qui permet de passer au tuto suivant
            //********************************************************
            //*******************NEXT*********************************
            //********************************************************
            Intent nextIntent = new Intent(context, InstartistWidget.class);

            // On veut que l'intent lance la mise à jour
            nextIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

            // On n'oublie pas les identifiants
            nextIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

            // On rajoute la direction
            nextIntent.putExtra(EXTRA_DIRECTION, EXTRA_NEXT);

            // Ainsi que l'indice
            nextIntent.putExtra(EXTRA_INDICE, indice);

            // Les données inutiles mais qu'il faut rajouter
            Uri data = Uri.withAppendedPath(Uri.parse("WIDGET://widget/id/"), String.valueOf(R.id.next));
            nextIntent.setData(data);

            // On insère l'intent dans un PendingIntent
            PendingIntent nextPending = PendingIntent.getBroadcast(context, 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            // Et on l'associe à l'activation du bouton
            views.setOnClickPendingIntent(R.id.next, nextPending);

            // La prochaine section est destinée au bouton qui permet de passer au tuto précédent
            //********************************************************
            //*******************PREVIOUS*****************************
            //********************************************************

            Intent previousIntent = new Intent(context, InstartistWidget.class);

            previousIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            previousIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            previousIntent.putExtra(EXTRA_DIRECTION, EXTRA_PREVIOUS);
            previousIntent.putExtra(EXTRA_INDICE, indice);

            data = Uri.withAppendedPath(Uri.parse("WIDGET://widget/id/"), String.valueOf(R.id.previous));
            previousIntent.setData(data);

            PendingIntent previousPending = PendingIntent.getBroadcast(context, 1, previousIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            views.setOnClickPendingIntent(R.id.previous, previousPending);
            // Et il faut mettre à jour toutes les vues
            appWidgetManager.updateAppWidget(appWidgetIds[i], views);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Si l'action est celle d'ouverture du tutoriel
        if(intent.getAction().equals(ACTION_OPEN_TUTO)) {
            Intent link = new Intent(Intent.ACTION_VIEW);
            link.setData(intent.getData());
            link.addCategory(Intent.CATEGORY_DEFAULT);
            // Comme on ne se trouve pas dans une activité, on demande à créer une nouvelle tâche
            link.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(link);
        } else {
            // Sinon, s'il s'agit d'une demande de mise à jour
            // On récupère l'indice passé en extra, ou -1 s'il n'y a pas d'indice
            int tmp = intent.getIntExtra(EXTRA_INDICE, -1);

            // S'il y avait bien un indice passé
            if(tmp != -1) {
                // On récupère la direction
                String extra = intent.getStringExtra(EXTRA_DIRECTION);
                // Et on calcule l'indice voulu par l'utilisateur
                if (extra.equals(EXTRA_PREVIOUS)) {
                    indice = (tmp - 1) % PUBLICATIONS.length;
                    if(indice < 0)
                        indice += PUBLICATIONS.length;
                } else if(extra.equals(EXTRA_NEXT))
                    indice = (tmp + 1) % PUBLICATIONS.length;
            }
        }

        // On revient au traitement naturel du Receiver, qui va lancer onUpdate s'il y a demande de mise à jour
        super.onReceive(context, intent);
    }
}

