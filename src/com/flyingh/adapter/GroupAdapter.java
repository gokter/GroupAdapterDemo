package com.flyingh.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class GroupAdapter<K, V extends Collection<E>, E extends Comparable<? super E>> extends BaseAdapter {
	private Context context;
	private Transformer<E, K> transformer;
	private boolean sortKey;
	private Comparator<K> keyComparator;
	private boolean sortValues;
	private Comparator<E> valueComparator;
	private List<Object> list = new ArrayList<Object>();
	private Set<Integer> keyPositions = new HashSet<Integer>();

	public static interface Transformer<E, K> {
		K transform(E e);
	}

	public GroupAdapter(Context context, Transformer<E, K> transformer, V v) {
		this(context, transformer, true, null, true, null, v);
	}

	public GroupAdapter(Context context, Transformer<E, K> transformer, boolean sortKey, Comparator<K> keyComparator, Boolean sortValues,
			Comparator<E> valueComparator, V v) {
		super();
		this.context = context;
		this.transformer = transformer;
		this.sortKey = sortKey;
		this.keyComparator = keyComparator;
		this.sortValues = sortValues;
		this.valueComparator = valueComparator;
		initData(v);
	}

	private void initData(V v) {
		if (v == null) {
			return;
		}
		Map<K, V> map = group(v);
		for (Map.Entry<K, V> me : map.entrySet()) {
			keyPositions.add(list.size());
			list.add(me.getKey());
			list.addAll(sortValues ? sortValues(me.getValue()) : me.getValue());
		}
	}

	@SuppressWarnings("unchecked")
	private Map<K, V> group(V v) {
		Map<K, V> map = sortKey ? new TreeMap<K, V>(keyComparator) : new LinkedHashMap<K, V>();
		for (E e : v) {
			K k = transformer.transform(e);
			if (map.get(k) == null) {
				try {
					map.put(k, (V) v.getClass().newInstance());
				} catch (Exception e1) {
					e1.printStackTrace();
					throw new RuntimeException(e1);
				}
			}
			map.get(k).add(e);
		}
		return map;
	}

	private List<E> sortValues(V v) {
		List<E> result = new ArrayList<>(v);
		Collections.sort(result, valueComparator);
		return result;
	}

	public void changeData(V v) {
		reset();
		initData(v);
		notifyDataSetChanged();
	}

	public void clear() {
		reset();
		notifyDataSetChanged();
	}

	private void reset() {
		list.clear();
		keyPositions.clear();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		if (convertView == null) {
			view = isKeyPosition(position) ? newKeyView(context, (K) getItem(position), parent)
					: newValueView(context, (E) getItem(position), parent);
		} else {
			view = convertView;
		}
		if (isKeyPosition(position)) {
			bindKeyView(view, context, (K) getItem(position));
		} else {
			bindValueView(view, context, (E) getItem(position));
		}
		return view;
	}

	public boolean isKeyPosition(int position) {
		return keyPositions.contains(position);
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		return isKeyPosition(position) ? 0 : 1;
	}

	public List<Object> getList() {
		return list;
	}

	public void setList(List<Object> list) {
		this.list = list;
	}

	public Set<Integer> getKeyPositions() {
		return keyPositions;
	}

	public void setKeyPositions(Set<Integer> keyPositions) {
		this.keyPositions = keyPositions;
	}

	public Transformer<E, K> getTransformer() {
		return transformer;
	}

	public void setTransformer(Transformer<E, K> transformer) {
		this.transformer = transformer;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public boolean isSortByKey() {
		return sortKey;
	}

	public void setSortByKey(boolean sortByKey) {
		this.sortKey = sortByKey;
	}

	public Comparator<K> getKeyComparator() {
		return keyComparator;
	}

	public void setKeyComparator(Comparator<K> keyComparator) {
		this.keyComparator = keyComparator;
	}

	public boolean isSortByValue() {
		return sortValues;
	}

	public void setSortByValue(boolean sortByValue) {
		this.sortValues = sortByValue;
	}

	public Comparator<E> getValueComparator() {
		return valueComparator;
	}

	public void setValueComparator(Comparator<E> valueComparator) {
		this.valueComparator = valueComparator;
	}

	public abstract View newKeyView(Context context, K item, ViewGroup parent);

	public abstract void bindKeyView(View view, Context context, K item);

	public abstract View newValueView(Context context, E item, ViewGroup parent);

	public abstract void bindValueView(View view, Context context, E item);
}
