package glbnt.com.coordinatorbottomsheetlib.utils;

/**
 * Created by ismaelvayra on 17/12/15.
 */
public class BottomSheetUtils {

    public static float getScaledAlpha(float position, float startPoint, float endPoint) {

        float alpha;
        if (position<startPoint) {
            alpha = 1;
        } else if (position >= startPoint && position <= endPoint) {
            alpha = 1-(1/(endPoint-startPoint))*position +startPoint/(endPoint-startPoint);
        } else {
            alpha = 0;
        }

        return alpha;
    }

    public static float getParallaxPosition(
            float position,
            float startPoint,
            float endPoint,
            float translationToDo,
            float actualTranslation) {

        float parallaxY;
        if (position<startPoint) {
            parallaxY = 0;
        } else if (position >= startPoint && position <= endPoint) {
            parallaxY = -(translationToDo/(endPoint-startPoint)) * position + ((translationToDo/(endPoint-startPoint))*startPoint);
        } else {
            parallaxY = actualTranslation;
        }

        return parallaxY;
    }

    public static float getScrollingHeight(
            float position,
            float startPoint,
            float endPoint,
            float translationToDo,
            float actualTranslation) {

        float parallaxY;
        if (position<startPoint) {
            parallaxY = actualTranslation;
        } else if (position >= startPoint && position <= endPoint) {
            parallaxY = -((translationToDo-actualTranslation)/(endPoint-startPoint)) * position + (((translationToDo-actualTranslation)/(endPoint-startPoint))*startPoint);
        } else {
            parallaxY = translationToDo;
        }

        return parallaxY;
    }

}
