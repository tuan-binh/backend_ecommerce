package ra.service.size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
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
	public SizeResponse findById(Long id) {
		Optional<Size> optionalSize = sizeRepository.findById(id);
		
		return null;
	}
	
	@Override
	public SizeResponse save(SizeRequest sizeRequest) {
		return null;
	}
	
	@Override
	public SizeResponse update(SizeRequest sizeRequest, Long id) {
		return null;
	}
	
	@Override
	public SizeResponse changeStatus(Long id) {
		return null;
	}
}
