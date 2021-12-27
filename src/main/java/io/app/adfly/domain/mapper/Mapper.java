package io.app.adfly.domain.mapper;

import io.app.adfly.domain.dto.PaginatedRequest;
import io.app.adfly.domain.dto.PaginatedResponse;
import io.app.adfly.domain.dto.ProductDto;
import io.app.adfly.domain.dto.UserDto;
import io.app.adfly.entities.Product;
import io.app.adfly.entities.User;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

public class Mapper {
    private static org.modelmapper.ModelMapper modelMapper;
    static {
        modelMapper = new org.modelmapper.ModelMapper();

        modelMapper.typeMap(Product.class, ProductDto.class).addMappings(mapper -> {
            mapper.map(src -> src.getProductRewarding(),
                    ProductDto::setProductRewarding);
        });
        modelMapper.typeMap(PaginatedRequest.class, Pageable.class).setConverter(PaginatedRequestToPageableConverter());

    }

    public static  <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        return source
                .stream()
                .map(element -> modelMapper.map(element, targetClass))
                .collect(Collectors.toList());
    }

    public static <T> T map(Object source, Class<T> destinationType){
        return modelMapper.map(source, destinationType);
    }

    public static void map(Object source, Object destination){
        modelMapper.map(source, destination);
    }

    public static <T> PaginatedResponse<T> mapPaginatedResponse(List<T> list, PaginatedRequest request, int totalCount){
        var paginateResponse = new PaginatedResponse<T>();
        paginateResponse.setData(list);
        paginateResponse.setStartAt(request.getStartAt());
        paginateResponse.setCount(request.getCount());
        paginateResponse.setTotalCount(totalCount);
        return paginateResponse;
    }

    private static AbstractConverter<PaginatedRequest, Pageable> PaginatedRequestToPageableConverter(){
        return new AbstractConverter<>() {
            @Override
            protected Pageable convert(PaginatedRequest request) {
                int pageNumber = request.getStartAt()/request.getCount();
                Pageable paging = PageRequest.of(pageNumber, request.getCount(), Sort.by("id"));
                return paging;
            }
        };

    }
}
