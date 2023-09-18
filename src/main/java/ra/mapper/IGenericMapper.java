package ra.mapper;

import ra.exception.*;

public interface IGenericMapper<T, K, L> {
	
	T toEntity(K k) throws ProductException, OrderException, CouponException, CategoryException, ColorException, SizeException;
	
	L toResponse(T t);
	
}
