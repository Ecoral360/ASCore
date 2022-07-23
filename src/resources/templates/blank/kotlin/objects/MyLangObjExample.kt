package mylang.objects

import org.ascore.lang.objects.ASCObject

/**
 * An example of an object for the MyLang programming language
 */
class MyLangObjExample : ASCObject<Any?> {
    /**
     * This method is often called to unwrap the object from the ASCore object to the Java object
     *
     * @return the value of the object
     */
    override fun getValue(): Any? = null
}