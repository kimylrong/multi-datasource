/*
 * ObjectProcessor.java 2014-7-10
 */
package com.comstar.mars.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 对象处理
 * 
 * @author Li,Rong
 * @version 1.0
 */
public class ObjectProcessor {
	public static <O, F> List<F> getFieldList(List<O> list,
			FieldValueGetter<O, F> fieldValueGetter) {
		List<F> fieldList = new ArrayList<F>();
		if (list == null || list.isEmpty()) {
			return fieldList;
		}

		for (O obj : list) {
			fieldList.add(fieldValueGetter.getValue(obj));
		}

		return fieldList;
	}

	public static <O, F> List<F> getFieldListUnique(List<O> list,
			FieldValueGetter<O, F> fieldValueGetter) {
		List<F> fieldList = new ArrayList<F>();
		if (list == null || list.isEmpty()) {
			return fieldList;
		}

		Set<F> fieldSet = new HashSet<F>();
		for (O obj : list) {
			fieldSet.add(fieldValueGetter.getValue(obj));
		}

		fieldList.addAll(fieldSet);
		return fieldList;
	}

	public static <O, F> Map<F, List<O>> groupByField(List<O> list,
			FieldValueGetter<O, F> fieldValueGetter) {
		Map<F, List<O>> map = new HashMap<F, List<O>>();
		if (list == null || list.isEmpty()) {
			return map;
		}

		for (O obj : list) {
			F field = fieldValueGetter.getValue(obj);
			if (map.containsKey(field)) {
				map.get(field).add(obj);
			} else {
				List<O> objList = new ArrayList<O>();
				objList.add(obj);
				map.put(field, objList);
			}
		}

		return map;
	}

	public static <O, F> O searchByField(List<O> list,
			FieldValueGetter<O, F> fieldValueGetter, F target) {
		if (list == null || list.isEmpty()) {
			return null;
		}

		for (O obj : list) {
			F field = fieldValueGetter.getValue(obj);
			if (target.equals(field)) {
				return obj;
			}
		}

		return null;
	}

	public static <O, F> Map<F, O> listToMap(List<O> list,
			FieldValueGetter<O, F> fieldValueGetter) {
		HashMap<F, O> map = new HashMap<F, O>();
		if (list == null || list.isEmpty()) {
			return map;
		}

		for (O obj : list) {
			map.put(fieldValueGetter.getValue(obj), obj);
		}

		return map;
	}

	public static interface FieldValueGetter<O, F> {
		F getValue(O obj);
	}
}
