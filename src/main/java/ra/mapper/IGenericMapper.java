package ra.mapper;

public interface IGenericMapper<T, K, L> {
	
	T toEntity(K k);
	
	L toResponse(T t);
	
}
