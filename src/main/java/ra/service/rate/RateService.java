package ra.service.rate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ra.exception.RateException;
import ra.mapper.rate.RateMapper;
import ra.model.domain.Rates;
import ra.model.dto.request.RateRequest;
import ra.model.dto.response.RateResponse;
import ra.repository.IRateRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RateService implements IRateService {
	
	@Autowired
	private IRateRepository rateRepository;
	@Autowired
	private RateMapper rateMapper;
	
	@Override
	public Page<RateResponse> findAll(Pageable pageable) {
		List<RateResponse> list = rateRepository.findAll(pageable).stream()
				  .map(item -> rateMapper.toResponse(item))
				  .collect(Collectors.toList());
		return new PageImpl<>(list, pageable, list.size());
	}
	
	@Override
	public RateResponse findById(Long id) throws RateException {
		Optional<Rates> optionalRates = rateRepository.findById(id);
		return optionalRates.map(item -> rateMapper.toResponse(item)).orElseThrow(() -> new RateException("rate not found"));
	}
	
	@Override
	public RateResponse save(RateRequest rateRequest) {
		return rateMapper.toResponse(rateRepository.save(rateMapper.toEntity(rateRequest)));
	}
	
	@Override
	public RateResponse update(RateRequest rateRequest, Long id) {
		Rates rates = rateMapper.toEntity(rateRequest);
		rates.setId(id);
		return rateMapper.toResponse(rateRepository.save(rates));
	}
	
	@Override
	public RateResponse changeStatus(Long id) throws RateException {
		Rates rates = findRatesById(id);
		rates.setStatus(!rates.isStatus());
		return rateMapper.toResponse(rateRepository.save(rates));
	}
	
	public Rates findRatesById(Long id) throws RateException {
		Optional<Rates> optionalRates = rateRepository.findById(id);
		return optionalRates.orElseThrow(() -> new RateException("rate not found"));
	}
	
}
