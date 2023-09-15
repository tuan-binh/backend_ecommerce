package ra.service.color;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ra.exception.ColorException;
import ra.mapper.color.ColorMapper;
import ra.model.domain.Color;
import ra.model.dto.request.ColorRequest;
import ra.model.dto.response.ColorResponse;
import ra.repository.IColorRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ColorService implements IColorService {
	
	@Autowired
	private IColorRepository colorRepository;
	@Autowired
	private ColorMapper colorMapper;
	
	@Override
	public Page<ColorResponse> findAll(Pageable pageable, Optional<String> colorName) {
		List<ColorResponse> list = colorName.map(s -> colorRepository.findAllByColorNameContaining(pageable, s).stream()
				  .map(item -> colorMapper.toResponse(item))
				  .collect(Collectors.toList())).orElseGet(() -> colorRepository.findAll(pageable).stream()
				  .map(item -> colorMapper.toResponse(item))
				  .collect(Collectors.toList()));
		return new PageImpl<>(list, pageable, list.size());
	}
	
	@Override
	public ColorResponse findById(Long id) throws ColorException {
		Optional<Color> optionalColor = colorRepository.findById(id);
		return optionalColor.map(item -> colorMapper.toResponse(item)).orElseThrow(() -> new ColorException("color not found"));
	}
	
	@Override
	public ColorResponse save(ColorRequest colorRequest) throws ColorException {
		if(colorRepository.existsByColorName(colorRequest.getColorName())) {
			throw new ColorException("color name is exists");
		}
		return colorMapper.toResponse(colorRepository.save(colorMapper.toEntity(colorRequest)));
	}
	
	@Override
	public ColorResponse update(ColorRequest colorRequest, Long id) {
		Color color = colorMapper.toEntity(colorRequest);
		color.setId(id);
		return colorMapper.toResponse(colorRepository.save(color));
	}
	
	@Override
	public ColorResponse changeStatus(Long id) throws ColorException {
		Color color = findColorById(id);
		color.setStatus(!color.isStatus());
		return colorMapper.toResponse(colorRepository.save(color));
	}
	
	public Color findColorById(Long id) throws ColorException {
		Optional<Color> optionalColor = colorRepository.findById(id);
		return optionalColor.orElseThrow(() -> new ColorException("color not found"));
	}
	
}
