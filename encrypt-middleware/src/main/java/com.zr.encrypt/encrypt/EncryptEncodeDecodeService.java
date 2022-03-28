package com.zr.encrypt.encrypt;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zr.encrypt.entity.EncryptDataResponse;
import com.zr.encrypt.entity.EncryptParameter;
import com.zr.encrypt.entity.QueryRequest;
import com.zr.encrypt.entity.WrapperRequest;
import com.zr.encrypt.enums.DataType;
import com.zr.encrypt.service.EncryptService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Service
@Slf4j
public class EncryptEncodeDecodeService {
	@Autowired
	private EncryptService encryptService;

	@Value("${encrypt.decodeBatch:100}")
	private int decodeBatch;
	@Value("${encrypt.default.encoder:zhourui@gmail-encode}")
	private String encoder;

	public Map<String, String> getEncodeValues(List<String> sources, DataType dataType) {
		Map<String, String> result = Maps.newHashMap();

		if (CollectionUtils.isEmpty(sources)) {
			return null;
		}
		for (String source : sources) {
			result.put(source, source);
		}

		List<List<String>> partition = Lists.partition(sources, 100);
		for (List<String> subs : partition) {
			//wrapper 根据配置加密或解密  耗时比直接加密解密长
			WrapperRequest wrapperRequest = new WrapperRequest();
			wrapperRequest.setUid(encoder);//解密时传输发起解密请求的用户ID或账号,无用户则传接口名称，用于解密溯源
			List<EncryptParameter> parameters = Lists.newArrayList();

			for (String source : subs) {
				EncryptParameter encryptParameter = new EncryptParameter();

				encryptParameter.setDataType(dataType);
				encryptParameter.setValue(source);
				encryptParameter.setCode(source);

				parameters.add(encryptParameter);
			}

			wrapperRequest.setEncryptParameters(parameters);
			Map<String, EncryptDataResponse> map = null;
			try {
				map = encryptService.storageData(wrapperRequest);
			} catch (Exception e) {
				log.error("EncryptServiceError, {}", subs, e);
				return result;
			}
			if (MapUtils.isEmpty(map)) {
				return result;
			}
			for (String key : map.keySet()) {
				result.put(key, map.get(key).getEncryptId());
			}
		}
		return result;
	}

	public String getEncodeValue(String source, DataType dataType, Predicate<String> filter) {
		if (StringUtils.isEmpty(source)) {
			return source;
		}
		if (filter != null && !filter.test(source)) {
			return source;
		}
		return getEncodeValues(Arrays.asList(source), dataType).get(source);
	}

	public List<String> getDecodeValues(List<String> sources) {
		if (CollectionUtils.isEmpty(sources)) {
			return sources;
		}
		//100个一次
		List<List<String>> partition = Lists.partition(sources, decodeBatch);
		List<String> result = Lists.newArrayList();

		for (List<String> sub : partition) {
			List<String> decodeValue = getDecodeValue(sub, null);
			if (CollectionUtils.isEmpty(decodeValue)) {
				result.addAll(sub);
				continue;
			}
			result.addAll(decodeValue);
		}
		return result;
	}

	public List<String> getDecodeValue(List<String> sources, Predicate<String> filter) {
		if (CollectionUtils.isEmpty(sources)) {
			return sources;
		}
		//wrapper 根据配置加密或解密  耗时比直接加密解密长
		QueryRequest queryRequest = new QueryRequest();
		queryRequest.setUid("zhourui@gmail-decode");//解密时传输发起解密请求的用户ID或账号,无用户则传接口名称，用于解密溯源
		queryRequest.setEncryptIds(sources);
		Map<String, EncryptDataResponse> map = null;
		try {
			map = encryptService.queryData(queryRequest);
		} catch (Exception e) {
			log.error("EncryptServiceError, {}", sources, e);
			return sources;
		}
		if (MapUtils.isEmpty(map)) {
			return sources;
		}
		List<String> result = Lists.newArrayList();
		for (String source : sources) {
			if (filter == null || filter.test(source)) {
				result.add(map.get(source).getContent());
			} else {
				result.add(source);
			}
		}
		return result;
	}

}
