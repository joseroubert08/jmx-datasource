package com.adamcrume.jmxmon.telemetry;

import static com.adamcrume.jmxmon.JMXMon.bundle;
import gov.nasa.arc.mct.components.AbstractComponent;
import gov.nasa.arc.mct.components.FeedProvider;
import gov.nasa.arc.mct.components.JAXBModelStatePersistence;
import gov.nasa.arc.mct.components.ModelStatePersistence;
import gov.nasa.arc.mct.components.PropertyDescriptor;
import gov.nasa.arc.mct.components.PropertyDescriptor.VisualControlDescriptor;
import gov.nasa.arc.mct.services.activity.TimeService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import com.adamcrume.jmxmon.component.StringPropertyEditor;

public class TelemetryComponent extends AbstractComponent implements
		FeedProvider {	
	public static final String TelemetryPrefix = "jmx:";
	private AtomicReference<TelemetryFeed> model = new AtomicReference<TelemetryFeed> (new TelemetryFeed());

	@Override
	public boolean isLeaf() {
		return false;
	}

	@Override
	protected <T> T handleGetCapability(Class<T> capability) {
		if (FeedProvider.class.isAssignableFrom(capability)) {
			return capability.cast(this);
		}
		if (ModelStatePersistence.class.isAssignableFrom(capability)) {
		    JAXBModelStatePersistence<TelemetryFeed> persistence = new JAXBModelStatePersistence<TelemetryFeed>() {
				@Override
				protected TelemetryFeed getStateToPersist() {
					return model.get();
				}

				@Override
				protected void setPersistentState(TelemetryFeed modelState) {
					model.set(modelState);
				}

				@Override
				protected Class<TelemetryFeed> getJAXBClass() {
					return TelemetryFeed.class;
				}
		        
			};
			
			return capability.cast(persistence);
		}
		return null;
	}

	@Override
	public String getLegendText() {
		return getDisplayName() + "\n" + getExternalKey();
	}
	
	@Override
	public int getMaximumSampleRate() {
		return 1;
	}

	@Override
	public String getSubscriptionId() {
		return TelemetryPrefix+getComponentId();
	}
	
	public TelemetryFeed getModel() {
		return model.get();
	}

	@Override
	public TimeService getTimeService() {
		return TimeServiceImpl.getInstance();
	}

	@Override
	public FeedType getFeedType() {
		return FeedType.FLOATING_POINT;
	}

	@Override
	public String getCanonicalName() {
		return getDisplayName();
	}

	@Override
	public RenderingInfo getRenderingInfo(Map<String, String> data) {
		String riAsString = data.get(FeedProvider.NORMALIZED_RENDERING_INFO);
		RenderingInfo ri = null;  
		assert data.get(FeedProvider.NORMALIZED_VALUE_KEY) != null : "The VALUE key is required for a valid status.";
		assert data.get(FeedProvider.NORMALIZED_TIME_KEY) != null : "The TIME key is required for a valid status.";
		ri = FeedProvider.RenderingInfo.valueOf(riAsString);   
		return ri;
	}

	@Override
	public long getValidDataExtent() {
		return System.currentTimeMillis();
	}
	
	@Override
	public boolean isPrediction() {
		return false;
	}

	@Override
	public boolean isNonCODDataBuffer() {
		return false;
	}

	@Override
    public List<PropertyDescriptor> getFieldDescriptors() {
        // Provide an ordered list of fields to be included in the MCT Platform's InfoView.
        List<PropertyDescriptor> fields = new ArrayList<PropertyDescriptor>();

        TelemetryFeed model = getModel();
        PropertyDescriptor description = new PropertyDescriptor(bundle.getString("feed.description.label"),
                new StringPropertyEditor(model, "description"), VisualControlDescriptor.TextField);
        description.setFieldMutable(true);
        fields.add(description);

        // TODO: Add validation
        PropertyDescriptor jmxURL = new PropertyDescriptor(bundle.getString("feed.jmxurl.label"),
                new StringPropertyEditor(model, "jmxURL"), VisualControlDescriptor.TextField);
        jmxURL.setFieldMutable(true);
        fields.add(jmxURL);

        // TODO: Add validation
        PropertyDescriptor mbean = new PropertyDescriptor(bundle.getString("feed.mbean.label"),
                new StringPropertyEditor(model, "mbean"), VisualControlDescriptor.TextField);
        mbean.setFieldMutable(true);
        fields.add(mbean);

        // TODO: Add validation
        PropertyDescriptor attribute = new PropertyDescriptor(bundle.getString("feed.attribute.label"),
                new StringPropertyEditor(model, "attribute"), VisualControlDescriptor.TextField);
        attribute.setFieldMutable(true);
        fields.add(attribute);

        return fields;
    }
}
