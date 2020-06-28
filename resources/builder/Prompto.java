package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Prompto extends _Default{
	public Prompto() {
		super("Prompto", "prom", "prom");
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{8384};
				break;
			case HP:
				ids = new int[]{8386};
				break;
			case S1:
				ids = new int[]{7986};
				break;
			case S2:
				ids = new int[]{7990};
				break;
			case EX:
				ids = new int[]{7998};
				break;
			case AA:
				ids = new int[]{4397};
				break;
			case LD:
				ids = new int[]{};
				break;
			case BT:
				ids = new int[]{};
				break;
			case CA:
				ids = new int[]{};
				break;
		}
		if(ids.length == 0)
			return super.getAbility(type);
		else
			return IntStream.of(ids).boxed().map(i -> this.getSpecificAbility(i)).collect(Collectors.toList());
	}
}