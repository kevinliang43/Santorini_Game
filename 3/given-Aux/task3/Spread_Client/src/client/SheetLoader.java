package client;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

/**
 * ClassLoader to dynamically load expected classes
 */
public class SheetLoader extends ClassLoader {

  private Class<?> Sheet;
  private Class<?> IValue;
  private Class<?> Constant;
  private Class<?> Reference;
  private Class<?> Function;
  private Class<?> IOperation;
  private Class<?> Addition;
  private Class<?> Multiplication;

  SheetLoader() throws ClassNotFoundException {

    this.Sheet = Class.forName(Client.SHEET_CLASS);
    this.IValue = Class.forName(Client.IVALUE_CLASS);
    this.Constant = Class.forName(Client.CONSTANT_CLASS);
    this.Reference = Class.forName(Client.REFERENCE_CLASS);
    this.Function = Class.forName(Client.FUNCTION_CLASS);
    this.IOperation = Class.forName(Client.IOPERATION_CLASS);
    this.Addition = Class.forName(Client.ADDITION_CLASS);
    this.Multiplication = Class.forName(Client.MULTIPLICATION_CLASS);

  }

  public Object invokeSheetConstructor(int x, int y) throws ClassNotFoundException,
          NoSuchMethodException, IllegalAccessException, InvocationTargetException,
          InstantiationException {
    return Sheet.getConstructor(int.class, int.class).newInstance(x, y);
  }

  public Object invokeConstantConstructor(double c) throws ClassNotFoundException,
          NoSuchMethodException, IllegalAccessException, InvocationTargetException,
          InstantiationException {
    return Constant.getConstructor(double.class).newInstance(c);
  }

  public Object invokeReferenceConstructor(int x, int y) throws ClassNotFoundException,
          NoSuchMethodException, IllegalAccessException, InvocationTargetException,
          InstantiationException {
    return Reference.getConstructor(int.class, int.class).newInstance(x, y);
  }
  public Object invokeFunctionConstructor(Object iv1, Object iv2, Object op) throws ClassNotFoundException,
          NoSuchMethodException, IllegalAccessException, InvocationTargetException,
          InstantiationException {
    return Function.getConstructor(IValue, IValue, IOperation).newInstance(iv1, iv2, op);
  }

  public Object invokeAdditionConstructor() throws ClassNotFoundException,
          NoSuchMethodException, IllegalAccessException, InvocationTargetException,
          InstantiationException {
    return Addition.getConstructor().newInstance();
  }

  public Object invokeMultiplicationConstructor() throws ClassNotFoundException,
          NoSuchMethodException, IllegalAccessException, InvocationTargetException,
          InstantiationException {
    return Multiplication.getConstructor().newInstance();
  }


  public Object invokeClassConstructor(String className, Object... args) throws ClassNotFoundException,
          NoSuchMethodException, IllegalAccessException, InvocationTargetException,
          InstantiationException {
    ClassLoader sheetLoader = this.getClass().getClassLoader();
    Class<?> loadedClass = sheetLoader.loadClass(className);

    Constructor constructor = loadedClass.getConstructor();
    return constructor.newInstance(args);
  }

  public void invokeMethod(Object o, String methodName, Object... args) throws NoSuchMethodException,
          InvocationTargetException, IllegalAccessException {
    Method m = o.getClass().getDeclaredMethod(methodName, Object[].class);
    m.invoke(o, args);
  }


  public String getValue(Object o, int x, int y) throws NoSuchMethodException,
          InvocationTargetException, IllegalAccessException {

    Method getValue = o.getClass().getDeclaredMethod("getValue", int.class, int.class);
    return getValue.invoke(o, x, y).toString();
  }

  public void setCell(Object o, int x, int y, Object v) throws NoSuchMethodException,
          InvocationTargetException, IllegalAccessException {

    Method setCell = o.getClass().getDeclaredMethod("setCell", int.class, int.class, IValue);
    setCell.invoke(o, x, y, v);
  }
}
