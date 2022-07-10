package mylang.objects;

import org.ascore.lang.objects.ASCObject;

/**
 * An example of an object for the MyLang programming language
 */
public class MyLangObjExample implements ASCObject<Object> {

    /**
     * This method is often called to unwrap the object from the ASCore object to the Java object
     *
     * @return the value of the object
     */
    @Override
    public Object getValue() {
        return null;
    }
}
