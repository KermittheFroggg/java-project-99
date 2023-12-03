FROM gradle:8.1.1-jdk17

WORKDIR /

COPY . .

RUN gradle installDist

RUN chmod +x ./build/install/demo/bin/demo

CMD ./build/install/demo/bin/demo