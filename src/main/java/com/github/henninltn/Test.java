package com.github.henninltn;

import static com.github.henninltn.parsercombinator.Generators.*;
import com.github.henninltn.parsercombinator.Parser;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 簡単なテスト用のクラス
 */
public class Test {
    public static void main(String[] args) {
        GlobalScope globalScope = new GlobalScope();
        Evaluator evaluator = new Evaluator(globalScope);
        try {
            parseTest((Parser)getPrivateField(evaluator, "number"), "123"          );
            parseTest((Parser)getPrivateField(evaluator, "expr"  ), "1 + 2"        );
            parseTest((Parser)getPrivateField(evaluator, "expr"  ), "123"          );
            parseTest((Parser)getPrivateField(evaluator, "expr"  ), "1 + 2 + 3"    );
            parseTest((Parser)getPrivateField(evaluator, "expr"  ), "1 - 2 - 3"    );
            parseTest((Parser)getPrivateField(evaluator, "expr"  ), "1 - 2 + 3"    );
            parseTest((Parser)getPrivateField(evaluator, "expr"  ), "2 * 3 + 4"    );
            parseTest((Parser)getPrivateField(evaluator, "expr"  ), "2 + 3 * 4"    );
            parseTest((Parser)getPrivateField(evaluator, "expr"  ), "100 / 10 / 2" );
            parseTest((Parser)getPrivateField(evaluator, "expr"  ), "( 2 + 3 ) * 4");
            parseTest((Parser)getPrivateField(evaluator, "assign"), "x = 2"        );
            parseTest((Parser)getPrivateField(evaluator, "expr"  ), "x + 2"        );
            parseTest((Parser)getPrivateField(evaluator, "expr"  ), "x = y"        );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * テスト用にプライベートなフィールドを取り出す
     * @param target_object 対象クラスのインスタンス
     * @param field_name
     * @return
     * @throws Exception
     */
    private static Object getPrivateField(Object target_object, String field_name) throws Exception {
        Class c = target_object.getClass();
        Field fld = c.getDeclaredField(field_name);
        fld.setAccessible(true);

        return fld.get(target_object);
    }

    /**
     * テスト用にプライベートなメソッドを呼び出す
     * @param target_object 対象クラスのインスタンス
     * @param field_name
     * @param classArray
     * @param args          対象メソッドに渡す引数を配列にしたもの
     * @return
     * @throws Exception
     */
    private static Object doPrivateMethod(Object target_object, String field_name, Class[] classArray, Object[] args) throws Exception {
        Class c = target_object.getClass();
        Method method = c.getDeclaredMethod(field_name, classArray);
        method.setAccessible(true);

        return method.invoke(target_object, args);
    }
}
