package com.baidu.bae.api.image.local;

import java.util.Collection;
import java.util.Map;

import com.baidu.bae.api.image.Annotate;
import com.baidu.bae.api.image.BaeImageService;
import com.baidu.bae.api.image.Composite;
import com.baidu.bae.api.image.Image;
import com.baidu.bae.api.image.QRCode;
import com.baidu.bae.api.image.Transform;
import com.baidu.bae.api.image.VCode;

public class BaeImageServiceLocalImpl implements BaeImageService {

	public BaeImageServiceLocalImpl(String ak, String sk, String host) {
		// TODO Auto-generated constructor stub
	}

	public byte[] applyTransform(Image image, Transform transform) {
		// TODO Auto-generated method stub
		return null;
	}

	public byte[] applyAnnotate(Image image, Annotate annotate) {
		// TODO Auto-generated method stub
		return null;
	}

	public byte[] applyQRCode(QRCode qrCode) {
		// TODO Auto-generated method stub
		return null;
	}

	public byte[] applyComposite(Collection<Composite> composites,
			int canvas_width, int canvas_height, int outputType, int quality) {
		// TODO Auto-generated method stub
		return null;
	}

	public byte[] applyComposite(Collection<Composite> composites) {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<String, String> verifyVCode(VCode vCode) {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<String, String> generateVCode(VCode vCode) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getRequestId() {
		// TODO Auto-generated method stub
		return 0;
	}

}
