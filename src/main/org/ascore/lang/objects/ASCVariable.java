package org.ascore.lang.objects;

public class ASCVariable implements ASCObject<Object> {
    private String name;
    private ASCObject<?> ascObject;

    public ASCVariable(String name, ASCObject<?> ascObject) {
        this.name = name;
        this.ascObject = ascObject;
    }
    //----------------- Getters && Setters -----------------//

    public ASCObject<?> getAscObject() {
        return ascObject;
    }

    public void setAscObject(ASCObject<?> ascObject) {
        this.ascObject = ascObject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public ASCVariable clone() {
        return new ASCVariable(name, ascObject);
    }

    @Override
    public Object getValue() {
        return getAscObject().getValue();
    }

    @Override
    public String toString() {
        return this.getAscObject().toString();
    }
}
