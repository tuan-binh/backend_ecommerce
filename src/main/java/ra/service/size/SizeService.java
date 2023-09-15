package ra.service.size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ra.exception.SizeException;
import ra.mapper.size.SizeMapper;
import ra.model.domain.Size;
import ra.model.dto.request.SizeRequest;
import ra.model.dto.response.SizeResponse;
import ra.repository.ISizeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SizeService implements ISizeService {
	
	@Autowired
	private ISizeRepository sizeRepository;
	@Autowired
	private SizeMapper sizeMapper;
	
	@Override
	public Page<SizeResponse> findAll(Pageable pageable, Optional<String> sizeName) {
		List<SizeResponse> list = new ArrayList<>();
		list = sizeName.map(s -> sizeRepository.findAllBySizeName(pageable, s).stream()
				  .map(size -> sizeMapper.toResponse(size))
				  .collect(Collectors.toList())).orElseGet(() -> sizeRepository.findAll(pageable).stream()
				  .map(size -> sizeMapper.toResponse(size))
				  .collect(Collectors.toList()));
		return new PageImpl<>(list, pageable, list.size());
	}
	
	@Override
	public SizeResponse findById(Long id) throws SizeException {
		Optional<Size> optionalSize = sizeRepository.findById(id);
		return optionalSize.map(item -> sizeMapper.toResponse(item)).orElseThrow(() -> new SizeException("not found size"));
	}
	
	@Override
	public SizeResponse save(SizeRequest sizeRequest) {
		return sizeMapper.toResponse(sizeRepository.save(sizeMapper.toEntity(sizeRequest)));
	}
	
	@Override
	public SizeResponse update(SizeRequest sizeRequest, Long id) {
		Size size = sizeMapper.toEntity(sizeRequest);
		size.setId(id);
		return sizeMapper.toResponse(sizeRepository.save(size));
	}
	
	@Override
	public SizeResponse changeStatus(Long id) throws SizeException {
		Size size = findSizeById(id);
		size.setStatus(!size.isStatus());
		return sizeMapper.toResponse(sizeRepository.save(size));
	}
	
	public Size findSizeById(Long id) throws SizeException {
		Optional<Size> optionalSize = sizeRepository.findById(id);
		return optionalSize.orElseThrow(() -> new SizeException("not found size"));
	}
	
}
