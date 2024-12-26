/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.producingwebservice;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.spring.guides.gs_producing_web_service.GetCountryRequest;
import io.spring.guides.gs_producing_web_service.AddCountryRequest;
import io.spring.guides.gs_producing_web_service.Country;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.util.ClassUtils;
import org.springframework.ws.client.core.WebServiceTemplate;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProducingWebServiceApplicationIntegrationTests {

	private Jaxb2Marshaller getMarshaller = new Jaxb2Marshaller();
	private Jaxb2Marshaller addMarshaller = new Jaxb2Marshaller();

	@LocalServerPort
	private int port = 0;

	@BeforeEach
	public void init() throws Exception {
		getMarshaller.setPackagesToScan(ClassUtils.getPackageName(GetCountryRequest.class));
		getMarshaller.afterPropertiesSet();
		addMarshaller.setPackagesToScan(ClassUtils.getPackageName(AddCountryRequest.class));
		addMarshaller.afterPropertiesSet();
	}

	@Test
	public void getSendAndReceive() {
		WebServiceTemplate ws = new WebServiceTemplate(getMarshaller);
		GetCountryRequest request = new GetCountryRequest();
		request.setName("Spain");
		assertThat(ws.marshalSendAndReceive("http://localhost:"
				+ port + "/ws", request) != null);
    }
	@Test
	public void addSendAndReceive() {
		WebServiceTemplate ws = new WebServiceTemplate(addMarshaller);
		AddCountryRequest request = new AddCountryRequest();
		Country TestCountry = new Country();
		TestCountry.setCapital("testcity");
		request.setCountry(TestCountry);
		assertThat(ws.marshalSendAndReceive("http://localhost:"
				+ port + "/add", request) != null);
	}
}
