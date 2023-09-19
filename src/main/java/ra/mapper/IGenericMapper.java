package ra.mapper;

import ra.exception.*;

public interface IGenericMapper<T, K, L> {
	
	T toEntity(K k) throws ProductException, OrderException, CouponException, CategoryException, ColorException, SizeException, ProductDetailException;
	
	L toResponse(T t);
	
}
