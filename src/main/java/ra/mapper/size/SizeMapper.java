package ra.mapper.size;

import org.springframework.stereotype.Component;
import ra.mapper.IGenericMapper;
import ra.model.domain.Size;
import ra.model.dto.request.SizeRequest;
import ra.model.dto.response.SizeResponse;

@Component
public class SizeMapper implements IGenericMapper<Size, SizeRequest, SizeResponse> {
	
	@Override
	public Size toEntity(SizeRequest sizeRequest) {
		return Size.builder()
				  .sizeName(sizeRequest.getSizeName())
				  .status(sizeRequest.isStatus())
				  .build();
	}
	
	@Override
	public SizeResponse toResponse(Size size) {
		return SizeResponse.builder()
				  .id(size.getId())
				  .sizeName(size.getSizeName())
				  .status(size.isStatus())
				  .build();
	}
}
