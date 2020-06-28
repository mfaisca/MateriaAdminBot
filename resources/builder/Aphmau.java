package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Aphmau extends _Default{
	public Aphmau() {
		super("Aphmau", "aph");
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{8448};
				break;
			case HP:
				ids = new int[]{8449};
				break;
			case S1:
				ids = new int[]{2606};
				break;
			case S2:
				ids = new int[]{2608};
				break;
			case EX:
				ids = new int[]{8445, 8615};
				break;
			case AA:
				ids = new int[]{4932};
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