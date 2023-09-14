package ra.mapper.color;

import org.springframework.stereotype.Component;
import ra.mapper.IGenericMapper;
import ra.model.domain.Color;
import ra.model.dto.request.ColorRequest;
import ra.model.dto.response.ColorResponse;

@Component
public class ColorMapper implements IGenericMapper<Color, ColorRequest, ColorResponse> {
	
	@Override
	public Color toEntity(ColorRequest colorRequest) {
		return Color.builder()
				  .colorName(colorRequest.getColorName())
				  .status(colorRequest.isStatus())
				  .build();
	}
	
	@Override
	public ColorResponse toResponse(Color color) {
		return ColorResponse.builder()
				  .id(color.getId())
				  .colorName(color.getColorName())
				  .status(color.isStatus())
				  .build();
	}
}
