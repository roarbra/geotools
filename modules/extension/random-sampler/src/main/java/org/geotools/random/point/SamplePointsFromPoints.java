package org.geotools.random.point;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.util.FeatureStreams;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.type.GeometryDescriptor;

/**
 * Sampling random points from point features.
 * 
 * Specify number of points to draw.
 * 
 * 
 * @author Roar Brænden
 *
 */
public class SamplePointsFromPoints implements Iterable<Point>{
	
	private int numPoints;
	
	private final SimpleFeatureCollection features;
	
	private int size = -1;
	
	/** Draw samples from the features. Feature's default geometry must be Point. */
	public SamplePointsFromPoints(SimpleFeatureCollection features){
		GeometryDescriptor descriptor = features.getSchema().getGeometryDescriptor();
		if (descriptor.getType().getBinding() != Point.class) {
			throw new IllegalArgumentException("SamplePointsFromPoints will only allow features with Point geometry.");
		}
		this.features = features;
	}

	public int getNumPoints() {
		return numPoints;
	}

	public void setNumPoints(int numPoints) {
		this.numPoints = numPoints;
	}

	@Override
	public Iterator<Point> iterator() {
		return new SamplePointsIterator(numPoints);
	}

	/**
	 * Iterator using the collections SimpleFeatureIterator.
	 */
	public class SamplePointsIterator implements Iterator<Point> {

		private final int num;
		private int inx = -1;
		private int sampledIter = -1;
		private final List<Integer> sampled;
		private SimpleFeatureIterator origIterator;
		
		SamplePointsIterator(int num) {
			this.num = num;
			if (size == -1) {
				size = features.size();
				if (size == -1) {
					size = (int)FeatureStreams.toFeatureStream(features).count();
				}
			}
			Set<Integer> temporary = new HashSet<Integer>();
			for (int i = 0; i < num; i++) {
				int u;
				do {
					u = (int)Math.floor(Math.random() * size);
				} while (temporary.contains(u));
				temporary.add(u);
			}
			sampled = temporary.stream().sorted().collect(Collectors.toList());
		}
		
		@Override
		public boolean hasNext() {
			return inx < num;
		}

		@Override
		public Point next() {
			if (inx++ == num) {
				throw new NoSuchElementException("You reach limit of sample points.");
			}
			for (int sInx = sampled.get(inx); sampledIter < sInx; origIterator.next());
			Point pnt = (Point)origIterator.next().getDefaultGeometry();
			if (inx == num - 1) {
				origIterator.close();
			}
			return pnt;
		}
	}
}
